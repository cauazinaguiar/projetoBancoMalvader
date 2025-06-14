package com.bancomalvader.util;

public class CpfValidator {

    /**
     * Valida o formato básico de um CPF (11 dígitos numéricos).
     * Nota: Esta é uma validação de formato e não de dígitos verificadores.
     * Para validação completa de CPF, uma lógica mais complexa seria necessária.
     * @param cpf O CPF a ser validado (apenas números).
     * @return True se o CPF tem 11 dígitos numéricos, false caso contrário.
     */
    public static boolean isValidFormat(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        // Verifica se tem 11 dígitos
        return cpf.length() == 11 && cpf.matches("\\d+");
    }

    // Você pode adicionar aqui uma lógica mais complexa para validar os dígitos verificadores do CPF.
    // Exemplo de como seria (sem implementação completa):
    /*
    public static boolean isValid(String cpf) {
        if (!isValidFormat(cpf)) {
            return false;
        }
        // Lógica de cálculo dos dígitos verificadores
        // ...
        return true; // Se os dígitos baterem
    }
    */
}
