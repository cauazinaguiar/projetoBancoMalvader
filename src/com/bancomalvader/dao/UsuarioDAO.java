package com.bancomalvader.dao;

import com.bancomalvader.model.Usuario;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioDAO {

    /**
     * Insere um novo usuário no banco de dados.
     * @param usuario O objeto Usuario a ser inserido.
     * @return O ID gerado para o novo usuário, ou -1 em caso de falha.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) VALUES (?, ?, ?, ?, ?, ?)";
        int idUsuarioGerado = -1;

        // Tenta obter uma conexão. Usamos try-with-resources para garantir o fechamento.
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getDataNascimento().format(DateTimeFormatter.ISO_DATE)); // Formata LocalDate para YYYY-MM-DD
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getTipoUsuario());
            stmt.setString(6, usuario.getSenhaHash());

            int rowsAffected = stmt.executeUpdate(); // Executa a inserção
            if (rowsAffected > 0) {
                // Obtém o ID gerado automaticamente (se a coluna for AUTO_INCREMENT)
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idUsuarioGerado = rs.getInt(1);
                        usuario.setIdUsuario(idUsuarioGerado); // Atualiza o objeto Model com o ID
                    }
                }
            }
        }
        return idUsuarioGerado;
    }

    /**
     * Verifica se um CPF já existe na tabela de usuários.
     * @param cpf O CPF a ser verificado.
     * @return True se o CPF existe, false caso contrário.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public boolean cpfExiste(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Busca um usuário pelo CPF e tipo de usuário.
     * @param cpf O CPF do usuário.
     * @param tipoUsuario O tipo de usuário ("FUNCIONARIO" ou "CLIENTE").
     * @return O objeto Usuario encontrado, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Usuario buscarUsuarioPorCpfETipo(String cpf, String tipoUsuario) throws SQLException {
        String sql = "SELECT id_usuario, nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash, otp_ativo, otp_expiracao FROM usuario WHERE cpf = ? AND tipo_usuario = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, tipoUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nome = rs.getString("nome");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String telefone = rs.getString("telefone");
                    String senhaHash = rs.getString("senha_hash");
                    String otpAtivo = rs.getString("otp_ativo");
                    LocalDateTime otpExpiracao = rs.getTimestamp("otp_expiracao") != null ? rs.getTimestamp("otp_expiracao").toLocalDateTime() : null;

                    return new Usuario(id, nome, cpf, dataNascimento, telefone, tipoUsuario, senhaHash, otpAtivo, otpExpiracao);
                }
            }
        }
        return null;
    }

    /**
     * Atualiza o OTP e sua expiração para um usuário.
     * @param idUsuario O ID do usuário.
     * @param otp O novo OTP.
     * @param otpExpiracao A nova data/hora de expiração do OTP.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public void atualizarOtpUsuario(int idUsuario, String otp, LocalDateTime otpExpiracao) throws SQLException {
        String sql = "UPDATE usuario SET otp_ativo = ?, otp_expiracao = ? WHERE id_usuario = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, otp);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(otpExpiracao)); // Converte LocalDateTime para Timestamp SQL
            stmt.setInt(3, idUsuario);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca um usuário pelo ID.
     * @param idUsuario O ID do usuário.
     * @return O objeto Usuario encontrado, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Usuario buscarUsuarioPorId(int idUsuario) throws SQLException {
        String sql = "SELECT id_usuario, nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash, otp_ativo, otp_expiracao FROM usuario WHERE id_usuario = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String telefone = rs.getString("telefone");
                    String tipoUsuario = rs.getString("tipo_usuario");
                    String senhaHash = rs.getString("senha_hash");
                    String otpAtivo = rs.getString("otp_ativo");
                    LocalDateTime otpExpiracao = rs.getTimestamp("otp_expiracao") != null ? rs.getTimestamp("otp_expiracao").toLocalDateTime() : null;

                    return new Usuario(idUsuario, nome, cpf, dataNascimento, telefone, tipoUsuario, senhaHash, otpAtivo, otpExpiracao);
                }
            }
        }
        return null;
    }
}
