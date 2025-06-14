package com.bancomalvader.dao;

import com.bancomalvader.model.Funcionario;
import com.bancomalvader.util.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class FuncionarioDAO {
	
	public int inserirFuncionario(Funcionario funcionario) throws SQLException {
		String sql = "INSERT INTO funcionario (id_usuario,codigo_funcionario, cargo, id_supervisor) VALUES (?, ?, ?, ?)";
		int idFuncionarioGerado = -1;
		
		try (Connection conn = ConexaoBanco.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setInt(1, funcionario.getIdUsuario());
			stmt.setString(2, funcionario.getCodigoFuncionario());
			stmt.setString(3, funcionario.getCargo());
			
			  if (funcionario.getIdSupervisor() != null) {
	                stmt.setInt(4, funcionario.getIdSupervisor());
	            } else {
	                stmt.setNull(4, Types.INTEGER); 
	            }
			
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try(ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						idFuncionarioGerado = rs.getInt(1);
						funcionario.setIdFuncionario(idFuncionarioGerado);
					}
				}
			}
		}
		return idFuncionarioGerado;
	}
	public Funcionario buscarFuncionarioPorId(int idFuncionario) throws SQLException {
		String sql = "SELECT id_funcionario, id_usuario, codigo_funcionario, cargo, id_supervisor FROM funcionario WHERE id_funcionario = ?";
		
		try (Connection conn = ConexaoBanco.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, idFuncionario);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int idUsuario = rs.getInt("id_usuario");
					String codigoFuncionario = rs.getString("codigo_funcionario");
					String cargo = rs.getString("cargo");
					Integer idSupervisor = (Integer) rs.getObject("id_supervisor");
					
					return new Funcionario(idFuncionario, idUsuario,codigoFuncionario, cargo, idSupervisor);
				}
			}
		}
		return null;
	}
}
