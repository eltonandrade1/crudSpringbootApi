package com.elton.springboot.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.elton.springboot.event.RecursoCriadoEvent;
import com.elton.springboot.model.Pessoa;
import com.elton.springboot.repository.PessoaRepository;
import com.elton.springboot.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private PessoaService pessoaService;

	@GetMapping
	protected List<Pessoa> findAll() {
		return pessoaService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	protected ResponseEntity<Pessoa> salvarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaService.salvar(pessoa);

		// Adiconar header location através do recurso de publicação de eventos do
		// spring
		eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getId()));

		// Exibe o objeto salvo no body html
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);

	}

	@GetMapping("/{id}")
	protected ResponseEntity<Pessoa> getById(@PathVariable Long id) {
		Pessoa pessoa = pessoaService.buscarPessoa(id);
		return ResponseEntity.ok(pessoa);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	protected void remover(@PathVariable Long id) {
		pessoaService.remover(id);
	}
	
	//TODO Consertar
//	@GetMapping("/nome")
//	protected ResponseEntity<Pessoa> findByNome(@RequestBody String nome) {
//		Pessoa pessoa = pessoaRepository.findByNome(nome);
//		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
//	}
	
	@PutMapping("/{id}")
	protected ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaService.atualizar(id, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{id}/status")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean status) {
		pessoaService.atualizarPropriedadeAtivo(id, status);

	}

}
