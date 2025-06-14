package com.bancomalvader.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Classe que representa a entidade 'usuario' do banco de dados
public class Usuario {
    private int idUsuario;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento; // Usar LocalDate para datas sem hora
    private String telefone;
    private String tipoUsuario; // Ex: "FUNCIONARIO", "CLIENTE"
    private String senhaHash;   // Hash da senha (MD5)
    private String otpAtivo;    // OTP atual
    private LocalDateTime otpExpiracao; // Data/hora de expiração do OTP

    // Construtor completo (para quando for carregar do banco)
    public Usuario(int idUsuario, String nome, String cpf, LocalDate dataNascimento,
                   String telefone, String tipoUsuario, String senhaHash,
                   String otpAtivo, LocalDateTime otpExpiracao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.senhaHash = senhaHash;
        this.otpAtivo = otpAtivo;
        this.otpExpiracao = otpExpiracao;
    }

    // Construtor para novas instâncias (sem ID, OTP - que são gerados pelo banco ou sistema)
    public Usuario(String nome, String cpf, LocalDate dataNascimento,
                   String telefone, String tipoUsuario, String senhaHash) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.senhaHash = senhaHash;
    }

    // Getters e Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getOtpAtivo() {
        return otpAtivo;
    }

    public void setOtpAtivo(String otpAtivo) {
        this.otpAtivo = otpAtivo;
    }

    public LocalDateTime getOtpExpiracao() {
        return otpExpiracao;
    }

    public void setOtpExpiracao(LocalDateTime otpExpiracao) {
        this.otpExpiracao = otpExpiracao;
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "idUsuario=" + idUsuario +
               ", nome='" + nome + '\'' +
               ", cpf='" + cpf + '\'' +
               ", dataNascimento=" + dataNascimento +
               ", telefone='" + telefone + '\'' +
               ", tipoUsuario='" + tipoUsuario + '\'' +
               // Evitar imprimir senhaHash em logs de produção por segurança
               ", otpAtivo='" + otpAtivo + '\'' +
               ", otpExpiracao=" + otpExpiracao +
               '}';
    }
}
