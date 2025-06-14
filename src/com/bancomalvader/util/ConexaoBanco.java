package com.bancomalvader.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    // URL de conexão com o banco de dados MySQL
    // Substitua 'localhost:3306' se seu MySQL estiver em outro servidor/porta
    // Substitua 'bancomalvader' se o nome do seu banco for diferente
    private static final String URL = "jdbc:mysql://localhost:3306/bancomalvader?useTimezone=true&serverTimezone=UTC";
    private static final String USUARIO = "root"; // Substitua pelo seu usuário MySQL
    private static final String SENHA = "Dorminhoco12.";   // Substitua pela sua senha MySQL

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados.
     * @return Objeto Connection para o banco de dados.
     * @throws SQLException Se ocorrer um erro de conexão ou driver.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carrega a classe do driver JDBC para MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Tenta estabelecer a conexão
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            // Erro se o driver não for encontrado (JAR não adicionado ao classpath)
            System.err.println("Erro: Driver JDBC do MySQL não encontrado. Certifique-se de que o JAR está no classpath.");
            throw new SQLException("Erro ao carregar o driver do banco de dados.", e);
        }
    }

    /**
     * Fecha uma conexão com o banco de dados de forma segura.
     * @param conn A conexão a ser fechada.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}
