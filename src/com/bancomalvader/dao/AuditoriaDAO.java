package com.bancomalvader.dao;

import com.bancomalvader.model.Auditoria;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter; // Para formatar LocalDateTime

public class AuditoriaDAO {

    /**
     * Registra uma nova entrada de auditoria no banco de dados.
     * @param auditoria O objeto Auditoria a ser registrado.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public void registrarAuditoria(Auditoria auditoria) throws SQLException {
        String sql = "INSERT INTO auditoria (id_usuario, acao, data_hora, detalhes) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (auditoria.getIdUsuario() != null) {
                stmt.setInt(1, auditoria.getIdUsuario());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER); // Define como NULL se não houver ID de usuário
            }
            stmt.setString(2, auditoria.getAcao());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(auditoria.getDataHora())); // Converte LocalDateTime
            stmt.setString(4, auditoria.getDetalhes());

            stmt.executeUpdate(); // Executa a inserção
        }
    }

    // Você pode adicionar métodos para consultar registros de auditoria aqui (ex: por usuário, por período).
}
