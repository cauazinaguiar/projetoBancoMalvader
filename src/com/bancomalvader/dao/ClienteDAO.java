package com.bancomalvader.dao;

import com.bancomalvader.model.Cliente;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDAO {

    /**
     * Insere um novo cliente no banco de dados.
     * @param cliente O objeto Cliente a ser inserido.
     * @return O ID gerado para o novo cliente, ou -1 em caso de falha.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int inserirCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (id_usuario, score_credito) VALUES (?, ?)";
        int idClienteGerado = -1;

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cliente.getIdUsuario());
            stmt.setDouble(2, cliente.getScoreCredito()); // Score inicial geralmente 0

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idClienteGerado = rs.getInt(1);
                        cliente.setIdCliente(idClienteGerado);
                    }
                }
            }
        }
        return idClienteGerado;
    }

    // Você pode adicionar métodos para buscar, atualizar e deletar clientes aqui.
    // Ex: buscarClientePorUsuarioId, atualizarScoreCredito, etc.
}
