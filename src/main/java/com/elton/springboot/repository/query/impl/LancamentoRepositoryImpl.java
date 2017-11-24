package com.elton.springboot.repository.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.elton.springboot.model.Categoria_;
import com.elton.springboot.model.Lancamento;
import com.elton.springboot.model.Lancamento_;
import com.elton.springboot.model.Pessoa_;
import com.elton.springboot.repository.filter.LancamentoFilter;
import com.elton.springboot.repository.projection.ResumoLancamento;
import com.elton.springboot.repository.query.LancamentoRepositoryQuery;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {
	
	/**
	 * Habilitar metamodel
	 * 
	 * 1 - Adicionar biblioteca hibernate-jpamodelgen ao pom do springboot
	 * 2 - Botao direito sobre o projeto >>> propriedades
	 * 3 - Java Compiler >>> Annotation Processing
	 * 	- habilitar Enable project specific settings
	 * 	- habilitar Enable annotation processing
	 * 	- habilitar Enable processing in editor
	 * 	- Em Generated source directory colocar src/main/java
	 * 4 - Java Compiler >>> Annotation Processing >>> Factory Path
	 * 	- Add External Jars e apontar para a biblioteca hibernate-jpamodelgen no diretório .m2
	 * 
	 * @author elton
	 */
	
	@PersistenceContext
	EntityManager entityManager; 

	@Override
	public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataPagamentoIni() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataPagamento), lancamentoFilter.getDataPagamentoIni()));
		}

		if (lancamentoFilter.getDataPagamentoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataPagamento), lancamentoFilter.getDataPagamentoFim()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class
				, root.get(Lancamento_.id), root.get(Lancamento_.descricao)
				, root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento)
				, root.get(Lancamento_.valor), root.get(Lancamento_.tipo)
				, root.get(Lancamento_.categoria).get(Categoria_.nome)
				, root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);
		
		return query.getResultList();
	}

}
