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
import com.elton.springboot.model.Lancamento;
import com.elton.springboot.repository.filter.LancamentoFilter;
import com.elton.springboot.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@GetMapping
	protected List<Lancamento> pesquisar(LancamentoFilter filter) {
		return lancamentoService.pesquisar(filter);
	}
	
	@GetMapping("/{id}")
	protected ResponseEntity<Lancamento> getById(@PathVariable Long id) {
		Lancamento lancamento = lancamentoService.buscarPessoa(id);
		return ResponseEntity.ok(lancamento); 
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	protected ResponseEntity<Lancamento> salvarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvarLancamento(lancamento);

		// Adiconar header location através do recurso de publicação de eventos do
		// spring
		eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));

		// Exibe o objeto salvo no body html
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);

	}
	
	@PutMapping("/{id}")
	protected ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento) {
		Lancamento lancamentoSalvo = lancamentoService.atualizar(id, lancamento);
		return ResponseEntity.ok(lancamentoSalvo);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	protected void remover(@PathVariable Long id) {
		lancamentoService.remover(id);
	}

}
