package com.bancomalvader.controller;

import com.bancomalvader.dao.AgenciaDAO;
import com.bancomalvader.dao.AuditoriaDAO;
import com.bancomalvader.dao.ClienteDAO;
import com.bancomalvader.dao.ContaDAO;
import com.bancomalvader.dao.EnderecoDAO;
import com.bancomalvader.dao.UsuarioDAO;
import com.bancomalvader.model.*; // Importa todas as classes do pacote model
import com.bancomalvader.util.ConexaoBanco; // Para transações
import com.bancomalvader.util.CpfValidator;
import com.bancomalvader.util.PasswordHasher;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class ContaController {

    private UsuarioDAO usuarioDAO;
    private EnderecoDAO enderecoDAO;
    private ClienteDAO clienteDAO;
    private AgenciaDAO agenciaDAO;
    private ContaDAO contaDAO;
    private AuditoriaDAO auditoriaDAO;

    public ContaController() {
        this.usuarioDAO = new UsuarioDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.clienteDAO = new ClienteDAO();
        this.agenciaDAO = new AgenciaDAO();
        this.contaDAO = new ContaDAO();
        this.auditoriaDAO = new AuditoriaDAO();
    }

    /**
     * Valida se um CPF já existe no sistema.
     * @param cpf O CPF a ser validado.
     * @return True se o CPF já existe, false caso contrário.
     */
    public boolean validarCpfExistente(String cpf) {
        try {
            return usuarioDAO.cpfExiste(cpf);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao validar CPF: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return true; // Considera como existente para evitar duplicação em caso de erro no banco
        }
    }

    /**
     * Abre uma nova conta bancária (Poupança, Corrente ou Investimento).
     * Esta operação é uma transação para garantir atomicidade.
     * @param nome Nome do cliente.
     * @param cpf CPF do cliente.
     * @param dataNascimento Data de nascimento do cliente.
     * @param telefone Telefone do cliente.
     * @param senha Senha do cliente (texto claro).
     * @param cep CEP do endereço.
     * @param logradouro Logradouro do endereço.
     * @param numeroCasa Número da casa.
     * @param bairro Bairro do endereço.
     * @param cidade Cidade do endereço.
     * @param estado Estado do endereço.
     * @param complemento Complemento do endereço (opcional).
     * @param codigoAgencia Código da agência.
     * @param tipoConta Tipo da conta ("POUPANCA", "CORRENTE", "INVESTIMENTO").
     * @param taxaRendimentoCP Taxa de rendimento para Conta Poupança.
     * @param limiteCC Limite para Conta Corrente.
     * @param dataVencimentoCC Data de vencimento para Conta Corrente.
     * @param taxaManutencaoCC Taxa de manutenção para Conta Corrente.
     * @param perfilRiscoCI Perfil de risco para Conta Investimento.
     * @param valorMinimoCI Valor mínimo para Conta Investimento.
     * @param taxaRendimentoBaseCI Taxa de rendimento base para Conta Investimento.
     * @return True se a conta foi aberta com sucesso, false caso contrário.
     */
    public boolean abrirConta(String nome, String cpf, LocalDate dataNascimento, String telefone,
                              String senha, String cep, String logradouro, int numeroCasa, String bairro, String cidade,
                              String estado, String complemento, String codigoAgencia, String tipoConta,
                              double taxaRendimentoCP, double limiteCC, LocalDate dataVencimentoCC,
                              double taxaManutencaoCC, String perfilRiscoCI, double valorMinimoCI,
                              double taxaRendimentoBaseCI, int idFuncionarioResponsavel) {

        // 1. Validações de entrada de dados (básicas, mais completas seriam na UI ou Models)
        if (nome.isEmpty() || !CpfValidator.isValidFormat(cpf) || dataNascimento == null || telefone.isEmpty() ||
            senha.isEmpty() || cep.isEmpty() || logradouro.isEmpty() || numeroCasa <= 0 || bairro.isEmpty() ||
            cidade.isEmpty() || estado.isEmpty() || codigoAgencia.isEmpty() || tipoConta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos obrigatórios e verifique os formatos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validação de senha forte (antes de hashear)
        if (senha.length() < 8 || !senha.matches(".*[A-Z].*") || !senha.matches(".*\\d.*") || !senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            JOptionPane.showMessageDialog(null, "A senha deve ter pelo menos 8 caracteres, 1 letra maiúscula, 1 número e 1 caractere especial.", "Erro de Senha", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Hashing da senha
        String senhaHash = PasswordHasher.hashPasswordMD5(senha);

        Connection conn = null; // Conexão para a transação
        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 2. Verificar se o CPF já existe
            if (usuarioDAO.cpfExiste(cpf)) {
                JOptionPane.showMessageDialog(null, "CPF já cadastrado no sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 3. Buscar ou criar agência (para simplificar, assumimos que a agência já existe ou será criada manualmente)
            Agencia agencia = agenciaDAO.buscarAgenciaPorCodigo(codigoAgencia);
            if (agencia == null) {
                 JOptionPane.showMessageDialog(null, "Agência com código " + codigoAgencia + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return false;
            }

            // 4. Inserir Usuário (como CLIENTE)
            Usuario novoUsuario = new Usuario(nome, cpf, dataNascimento, telefone, "CLIENTE", senhaHash);
            int idUsuario = usuarioDAO.inserirUsuario(novoUsuario);
            if (idUsuario == -1) {
                throw new SQLException("Falha ao inserir o usuário.");
            }

            // 5. Inserir Endereço para o novo usuário
            Endereco novoEndereco = new Endereco(0, idUsuario, cep, logradouro, numeroCasa, bairro, cidade, estado, complemento);
            int idEndereco = enderecoDAO.inserirEndereco(novoEndereco);
            if (idEndereco == -1) {
                throw new SQLException("Falha ao inserir o endereço.");
            }

            // 6. Inserir Cliente para o novo usuário
            Cliente novoCliente = new Cliente(0, idUsuario, 0.0); // Score inicial 0.0
            int idCliente = clienteDAO.inserirCliente(novoCliente);
            if (idCliente == -1) {
                throw new SQLException("Falha ao inserir o cliente.");
            }

            // 7. Gerar número da conta (Exemplo simples, o algoritmo de Luhn é mais complexo)
            String numeroConta = gerarNumeroContaAleatorio(); // Implemente um gerador de número de conta real

            // 8. Inserir Conta principal
            Conta novaConta = new Conta(0, numeroConta, agencia.getIdAgencia(), 0.0, tipoConta, idCliente, LocalDateTime.now(), "ATIVA");
            int idConta = contaDAO.inserirConta(novaConta, conn); // Passa a conexão da transação
            if (idConta == -1) {
                throw new SQLException("Falha ao inserir a conta principal.");
            }

            // 9. Inserir Detalhes da Conta Específica
            switch (tipoConta) {
                case "POUPANCA":
                    ContaPoupanca cp = new ContaPoupanca(0, idConta, taxaRendimentoCP, null);
                    contaDAO.inserirContaPoupanca(cp, conn); // Passa a conexão da transação
                    break;
                case "CORRENTE":
                    ContaCorrente cc = new ContaCorrente(0, idConta, limiteCC, dataVencimentoCC, taxaManutencaoCC);
                    contaDAO.inserirContaCorrente(cc, conn); // Passa a conexão da transação
                    break;
                case "INVESTIMENTO":
                    ContaInvestimento ci = new ContaInvestimento(0, idConta, perfilRiscoCI, valorMinimoCI, taxaRendimentoBaseCI);
                    contaDAO.inserirContaInvestimento(ci, conn); // Passa a conexão da transação
                    break;
            }

            // 10. Registrar Auditoria de Abertura de Conta
            String detalhesAuditoria = "Abertura de nova conta " + tipoConta + " para CPF: " + cpf + " (Número da Conta: " + numeroConta + ")";
            Auditoria auditoria = new Auditoria(idFuncionarioResponsavel, "ABERTURA_CONTA", LocalDateTime.now(), detalhesAuditoria);
            auditoriaDAO.registrarAuditoria(auditoria);

            conn.commit(); // Confirma todas as operações da transação
            JOptionPane.showMessageDialog(null, "Conta aberta com sucesso! Número da conta: " + numeroConta, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            // Em caso de erro, tenta fazer rollback da transação
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro ao fazer rollback da transação: " + rollbackEx.getMessage());
                }
            }
            JOptionPane.showMessageDialog(null, "Erro ao abrir conta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprime o stack trace para depuração
            return false;
        } finally {
            ConexaoBanco.closeConnection(conn); // Fecha a conexão
        }
    }

    /**
     * Gera um número de conta aleatório simples.
     * No futuro, implementar o algoritmo de Luhn para dígito verificador.
     * @return Um número de conta de 12 dígitos.
     */
    private String gerarNumeroContaAleatorio() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) { // 11 dígitos para o número base
            sb.append(rand.nextInt(10));
        }
        // Adicionar um dígito verificador simples (não Luhn) para exemplo
        int digitoVerificador = rand.nextInt(10);
        sb.append(digitoVerificador);
        return sb.toString();
    }
}
