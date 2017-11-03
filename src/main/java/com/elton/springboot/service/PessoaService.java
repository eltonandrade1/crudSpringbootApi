package com.elton.springboot.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.elton.springboot.model.Pessoa;
import com.elton.springboot.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Long id, Pessoa pessoa) {
		Pessoa pessoaSalva = getById(id);
		if(pessoaSalva != null) {
			// Copia as propriedades do objeto pessoa para pessoaSalva ignorando o id.
			BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		} else {
			throw new EmptyResultDataAccessException(1); 
		}
		return pessoaRepository.save(pessoaSalva);
	}
	
	public Pessoa buscarPessoa(Long id) {
		return getById(id);
	}
	
	public void atualizarPropriedadeAtivo(Long id, Boolean status) {
		Pessoa pessoaSalva = getById(id);
		pessoaSalva.setAtivo(status);
		pessoaRepository.save(pessoaSalva);
	}
	
	private Pessoa getById(Long id) {
		Pessoa pessoa = pessoaRepository.findOne(id);
		if(pessoa != null) {
			return pessoa;
		}
		throw new EmptyResultDataAccessException(1); 
	}
	
	public void remover(Long id) {
		pessoaRepository.delete(id);
	}

	public List<Pessoa> findAll() {
		return pessoaRepository.findAll();
	}

}
