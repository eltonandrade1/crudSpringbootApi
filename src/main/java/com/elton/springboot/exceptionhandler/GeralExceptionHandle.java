package com.elton.springboot.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.elton.springboot.exceptionhandler.bussinesexception.CategoriaInexistenteException;
import com.elton.springboot.exceptionhandler.bussinesexception.PessoaInexistenteOuInativaException;

@ControllerAdvice
public class GeralExceptionHandle extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	/**
	 * Método retorna mensagem de erro no serviço json, caso os atributos enviados
	 * nao atenda ao padrao /entidade desenvolvido
	 * 
	 * @author elton
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);

	}

	/**
	 * Método realiza a validação dos campos anotados com @NotNull nas entidades e
	 * tambem nos metodos anotados com @Valid
	 * 
	 * @author elton
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// Captura a lista de erros
		List<Erro> erros = getListaErros(ex.getBindingResult());

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Metodo retorna a lista de erros de validação em tempo de execução
	 * 
	 * @param bindingResult
	 * @return List<Erro>
	 * @author elton
	 */
	private List<Erro> getListaErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<>();

		for (FieldError fieldErro : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldErro, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldErro.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}

	/**
	 * Metodo para tratamento da exceção EmptyResultDataAccessException
	 * 
	 * @author elton
	 */
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}
	
	/**
	 * Método trata exceção DataIntegrityViolationException, utilzando ExceptionUtils, para melhorar 
	 * as informações geradas ao retornar a excecão
	 * 
	 * @author elton
	 */
	@ExceptionHandler({ DataIntegrityViolationException.class } )
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}
	
	/**
	 * Metodo para tratamento de exceções de negócios para um objeto pessoa
	 * inativo ou inexistente
	 * 
	 * @author elton
	 */
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);

	}
	
	/**
	 * Metodo para tratamento de exceções de negócios para um objeto categoria
	 * inexistente
	 * 
	 * @author elton
	 */
	@ExceptionHandler({ CategoriaInexistenteException.class })
	public ResponseEntity<Object> handleCategoriaInexistenteException(CategoriaInexistenteException ex) {
		String mensagemUsuario = messageSource.getMessage("categoria.inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);

	}

	/**
	 * Classe interna para tratamento das mensagens de exceção
	 * 
	 * @author elton
	 *
	 */
	public static class Erro {

		private String mensagemUsuario;

		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {

			this.mensagemUsuario = mensagemUsuario;

			this.mensagemDesenvolvedor = mensagemDesenvolvedor;

		}

		public String getMensagemUsuario() {

			return mensagemUsuario;

		}

		public String getMensagemDesenvolvedor() {

			return mensagemDesenvolvedor;

		}

	}
}
