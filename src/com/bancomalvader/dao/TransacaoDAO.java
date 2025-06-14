package com.bancomalvader.dao;

import com.bancomalvader.model.Transacao;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class TransacaoDAO {

	public int inserirTransacao(Transacao transacao) throws SQLException{
		String sql = "INSERT INTO transacao ( id_conta_origem, id_conta_destino, tipo_transacao, valor, data_hora, descricao) VALUES (?, ?, ?, ?, ?, ?)";
		int idTransacaoGerado = -1;
		
		try (Connection conn = ConexaoBanco.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setInt(1, transacao.getIdContaOrigem());
			stmt.setInt(2, transacao.getIdContaOrigem());
			stmt.setString(3, transacao.getTipoTransacao());
			stmt.setDouble(4, transacao.getValor());
			stmt.setTimestamp(5, java.sql.Timestamp.valueOf(transacao.getDataHora()));
			stmt.setString(6, transacao.getDescricao());
			
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()){
					if (rs.next()) {
						idTransacaoGerado = rs.getInt(1);
						transacao.setIdTransacao(idTransacaoGerado);
					}
				}
			}
		}
		return idTransacaoGerado;
	}
}
