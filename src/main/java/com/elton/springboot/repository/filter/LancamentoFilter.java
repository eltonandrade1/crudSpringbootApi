package com.elton.springboot.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LancamentoFilter {
	
	private String descricao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoIni;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoFim;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataPagamentoIni() {
		return dataPagamentoIni;
	}

	public void setDataPagamentoIni(LocalDate dataPagamentoIni) {
		this.dataPagamentoIni = dataPagamentoIni;
	}

	public LocalDate getDataPagamentoFim() {
		return dataPagamentoFim;
	}

	public void setDataPagamentoFim(LocalDate dataPagamentoFim) {
		this.dataPagamentoFim = dataPagamentoFim;
	}
	
}
