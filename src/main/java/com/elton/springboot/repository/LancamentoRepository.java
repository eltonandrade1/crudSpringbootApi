package com.elton.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
