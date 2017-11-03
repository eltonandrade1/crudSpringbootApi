package com.elton.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.elton.springboot.exceptionhandler.bussinesexception.CategoriaInexistenteException;
import com.elton.springboot.exceptionhandler.bussinesexception.PessoaInexistenteOuInativaException;
import com.elton.springboot.model.Categoria;
import com.elton.springboot.model.Lancamento;
import com.elton.springboot.model.Pessoa;
import com.elton.springboot.repository.CategoriaRepository;
import com.elton.springboot.repository.LancamentoRepository;
import com.elton.springboot.repository.PessoaRepository;

@Service
public class LancamentoService {
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	CategoriaRepository categoriaRepository;

	public List<Lancamento> findAll() {
		return lancamentoRepository.findAll();
	}
	
	public Lancamento buscarPessoa(Long id) {
		return getById(id);
	}
	

	private Lancamento getById(Long id) {
		Lancamento lancamento = lancamentoRepository.findOne(id);
		if(lancamento != null) {
			return lancamento;
		}
		throw new EmptyResultDataAccessException(1); 
	}

	public Lancamento salvarLancamento(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getId());
		Categoria categoria = categoriaRepository.findOne(lancamento.getCategoria().getId());
		
		//Validação para pessoa inativa ou inexistente		
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		//Validação para categoria inexistente
		if(categoria == null) {
			throw new CategoriaInexistenteException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

}
