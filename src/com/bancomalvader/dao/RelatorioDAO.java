package com.bancomalvader.dao;

import com.bancomalvader.model.Relatorio;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class RelatorioDAO {
	
	public int inserirRelatorio(Relatorio relatorio) throws SQLException {
		String sql = "INSERT INTO relatorio (id_funcionario, tipo_relatorio, data_geracao, conteudo) VALUES (?, ?, ?, ?)";
		int idRelatorioGerado = -1;
		
		try (Connection conn = ConexaoBanco.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setInt(1, relatorio.getIdFuncionario());
			stmt.setString(2, relatorio.getTipoRelatorio());
			stmt.setTimestamp(3, java.sql.Timestamp.valueOf(relatorio.getDatageracao()));
			
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						idRelatorioGerado = rs.getInt(1);
						relatorio.setIdRelatorio(idRelatorioGerado);
					}
				}
			}
		}
		return idRelatorioGerado;
	}
}
