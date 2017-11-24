package com.elton.springboot.repository.query;

import java.util.List;

import com.elton.springboot.model.Lancamento;
import com.elton.springboot.repository.filter.LancamentoFilter;
import com.elton.springboot.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter);
	public List<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter);


}
