package com.bancomalvader.dao;

import com.bancomalvader.model.Conta;
import com.bancomalvader.model.ContaCorrente;
import com.bancomalvader.model.ContaInvestimento;
import com.bancomalvader.model.ContaPoupanca;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter; // Para formatar LocalDate para String SQL

public class ContaDAO {

    /**
     * Insere uma nova conta principal na tabela 'conta'.
     * @param conta O objeto Conta a ser inserido.
     * @param conn A conexão com o banco de dados (para uso em transação).
     * @return O ID gerado para a nova conta, ou -1 em caso de falha.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int inserirConta(Conta conta, Connection conn) throws SQLException {
        String sql = "INSERT INTO conta (numero_conta, id_agencia, saldo, tipo_conta, id_cliente, data_abertura, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int idContaGerado = -1;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conta.getNumeroConta());
            stmt.setInt(2, conta.getIdAgencia());
            stmt.setDouble(3, conta.getSaldo());
            stmt.setString(4, conta.getTipoConta());
            stmt.setInt(5, conta.getIdCliente());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(conta.getDataAbertura())); // Converte LocalDateTime
            stmt.setString(7, conta.getStatus());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idContaGerado = rs.getInt(1);
                        conta.setIdConta(idContaGerado);
                    }
                }
            }
        }
        return idContaGerado;
    }

    /**
     * Insere uma nova conta poupança.
     * @param poupanca O objeto ContaPoupanca a ser inserido.
     * @param conn A conexão com o banco de dados (para uso em transação).
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public void inserirContaPoupanca(ContaPoupanca poupanca, Connection conn) throws SQLException {
        String sql = "INSERT INTO conta_poupanca (id_conta, taxa_rendimento, ultimo_rendimento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, poupanca.getIdConta());
            stmt.setDouble(2, poupanca.getTaxaRendimento());
            stmt.setTimestamp(3, poupanca.getUltimoRendimento() != null ? java.sql.Timestamp.valueOf(poupanca.getUltimoRendimento()) : null);
            stmt.executeUpdate();
        }
    }

    /**
     * Insere uma nova conta corrente.
     * @param corrente O objeto ContaCorrente a ser inserido.
     * @param conn A conexão com o banco de dados (para uso em transação).
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public void inserirContaCorrente(ContaCorrente corrente, Connection conn) throws SQLException {
        String sql = "INSERT INTO conta_corrente (id_conta, limite, data_vencimento, taxa_manutencao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, corrente.getIdConta());
            stmt.setDouble(2, corrente.getLimite());
            stmt.setString(3, corrente.getDataVencimento().format(DateTimeFormatter.ISO_DATE)); // Formata LocalDate
            stmt.setDouble(4, corrente.getTaxaManutencao());
            stmt.executeUpdate();
        }
    }

    /**
     * Insere uma nova conta investimento.
     * @param investimento O objeto ContaInvestimento a ser inserido.
     * @param conn A conexão com o banco de dados (para uso em transação).
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public void inserirContaInvestimento(ContaInvestimento investimento, Connection conn) throws SQLException {
        String sql = "INSERT INTO conta_investimento (id_conta, perfil_risco, valor_minimo, taxa_rendimento_base) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, investimento.getIdConta());
            stmt.setString(2, investimento.getPerfilRisco());
            stmt.setDouble(3, investimento.getValorMinimo());
            stmt.setDouble(4, investimento.getTaxaRendimentoBase());
            stmt.executeUpdate();
        }
    }

    /**
     * Busca uma conta pelo número da conta.
     * @param numeroConta O número da conta a ser buscada.
     * @return O objeto Conta encontrado, ou null se não for encontrada.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Conta buscarContaPorNumero(String numeroConta) throws SQLException {
        String sql = "SELECT id_conta, numero_conta, id_agencia, saldo, tipo_conta, id_cliente, data_abertura, status FROM conta WHERE numero_conta = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idConta = rs.getInt("id_conta");
                    int idAgencia = rs.getInt("id_agencia");
                    double saldo = rs.getDouble("saldo");
                    String tipoConta = rs.getString("tipo_conta");
                    int idCliente = rs.getInt("id_cliente");
                    java.sql.Timestamp dataAberturaTs = rs.getTimestamp("data_abertura");
                    String status = rs.getString("status");
                    return new Conta(idConta, numeroConta, idAgencia, saldo, tipoConta, idCliente, dataAberturaTs.toLocalDateTime(), status);
                }
            }
        }
        return null;
    }
}
