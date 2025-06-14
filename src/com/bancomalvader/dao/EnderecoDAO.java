package com.bancomalvader.dao;

import com.bancomalvader.model.Endereco;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EnderecoDAO {

    /**
     * Insere um novo endereço no banco de dados.
     * @param endereco O objeto Endereco a ser inserido.
     * @return O ID gerado para o novo endereço, ou -1 em caso de falha.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int inserirEndereco(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO endereco (id_usuario, cep, local, numero_casa, bairro, cidade, estado, complemento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int idEnderecoGerado = -1;

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, endereco.getIdUsuario());
            stmt.setString(2, endereco.getCep());
            stmt.setString(3, endereco.getLogradouro()); // 'local' no banco é 'logradouro' no modelo
            stmt.setInt(4, endereco.getNumeroCasa());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());
            stmt.setString(8, endereco.getComplemento());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idEnderecoGerado = rs.getInt(1);
                        endereco.setIdEndereco(idEnderecoGerado);
                    }
                }
            }
        }
        return idEnderecoGerado;
    }

    // Você pode adicionar métodos para buscar, atualizar e deletar endereços aqui.
    // Ex: buscarEnderecoPorUsuarioId, atualizarEndereco, etc.
}
