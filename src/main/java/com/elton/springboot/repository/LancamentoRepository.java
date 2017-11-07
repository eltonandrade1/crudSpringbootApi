package com.elton.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Lancamento;
import com.elton.springboot.repository.query.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
