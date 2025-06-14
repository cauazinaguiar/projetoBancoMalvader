package com.bancomalvader.model;

import java.time.LocalDate;

public class ContaCorrente {
	private int idContaCorrente;
	private int idConta;
	private double limite;
	private LocalDate dataVencimento;
	private double taxaManutencao;
	
	public ContaCorrente(int idContaCorrente, int idConta, double limite, LocalDate dataVencimento,
			double taxaManutencao) {
		
		this.idContaCorrente = idContaCorrente;
		this.idConta = idConta;
		this.limite = limite;
		this.dataVencimento = dataVencimento;
		this.taxaManutencao = taxaManutencao;
	}
	public ContaCorrente(int idConta, double limite, LocalDate dataVencimento, double taxaManutencao) {
		
		this.idConta = idConta;
		this.limite = limite;
		this.dataVencimento = dataVencimento;
		this.taxaManutencao = taxaManutencao;
	}
	public int getIdContaCorrente() {
		return idContaCorrente;
	}
	public void setIdContaCorrente(int idContaCorrente) {
		this.idContaCorrente = idContaCorrente;
	}
	public int getIdConta() {
		return idConta;
	}
	public void setIdConta(int idConta) {
		this.idConta = idConta;
	}
	public double getLimite() {
		return limite;
	}
	public void setLimite(double limite) {
		this.limite = limite;
	}
	public LocalDate getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public double getTaxaManutencao() {
		return taxaManutencao;
	}
	public void setTaxaManutencao(double taxaManutencao) {
		this.taxaManutencao = taxaManutencao;
	}
	@Override
	public String toString() {
		return "ContaCorrente [idContaCorrente=" + idContaCorrente + ", idConta=" + idConta + ", limite=" + limite
				+ ", dataVencimento=" + dataVencimento + ", taxaManutencao=" + taxaManutencao + "]";
	}
	
	
}
