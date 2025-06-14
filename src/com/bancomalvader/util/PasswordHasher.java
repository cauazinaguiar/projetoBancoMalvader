package com.bancomalvader.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHasher {

    /**
     * Gera o hash MD5 de uma senha.
     * @param password A senha em texto claro.
     * @return O hash MD5 da senha em formato hexadecimal (maiúsculas).
     */
    public static String hashPasswordMD5(String password) {
        // Usa a biblioteca Apache Commons Codec para gerar o hash MD5
        return DigestUtils.md5Hex(password).toUpperCase();
    }

    /**
     * Verifica se uma senha em texto claro corresponde a um hash MD5.
     * @param plainPassword A senha em texto claro.
     * @param hashedPassword O hash MD5 armazenado.
     * @return True se a senha corresponde ao hash, false caso contrário.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Gera o hash da senha em texto claro e compara com o hash armazenado
        return hashPasswordMD5(plainPassword).equals(hashedPassword);
    }

    // Nota: Para sistemas reais, MD5 é considerado inseguro para senhas.
    // Algoritmos como BCrypt ou Argon2 seriam mais apropriados.
    // Este método é implementado conforme a especificação do trabalho (MD5 ou superior).
}
