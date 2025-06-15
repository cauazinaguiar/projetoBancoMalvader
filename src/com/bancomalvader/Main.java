package com.bancomalvader; // Este arquivo fica no pacote base

import com.bancomalvader.view.LoginView; // Importa a tela de login

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Garante que a UI seja criada na Event Dispatch Thread (EDT)
        // Isso é crucial para aplicações Swing, garantindo que todas as operações
        // de UI sejam seguras para threads.
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(); // Instancia a tela de login
            loginView.setVisible(true); // Torna a tela de login visível, iniciando a aplicação
        });
    }
}
