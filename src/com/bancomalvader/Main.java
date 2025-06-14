package com.bancomalvader; // Este arquivo fica no pacote base

import com.bancomalvader.view.LoginView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Garante que a UI seja criada na Event Dispatch Thread (EDT)
        // Isso é crucial para aplicações Swing
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true); // Torna a tela de login visível
        });
    }
}
