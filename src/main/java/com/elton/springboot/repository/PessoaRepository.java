package com.elton.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	public List<Pessoa> findByNomeContaining(String nome);

}
