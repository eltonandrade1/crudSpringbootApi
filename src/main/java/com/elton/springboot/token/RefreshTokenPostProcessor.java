package com.elton.springboot.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.elton.springboot.config.property.PersonalApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{
	
	@Autowired
	private PersonalApiProperty personalApiProperty;

	/**
	 * Método intercepta as requisições do tipo OAuth2AccessToken e verifica se o método é
	 * do tipo postAccessToken para retornar true
	 * @author elton
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}
	
	/**
	 * Método só sera executado se o método supports retornar true, onde em seguida pega o refresh token e 
	 * adiciona a um cookie https
	 * @author elton
	 */
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		//Converçao de ServerHttpRequest e ServerHttpResponse para HttpServletRequest e HttpServletResponse
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		//Cast do OAuth2AccessToken para poder remover o token do body
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		//Pegando RefreshToken da requisiçao
		String refreshToken = body.getRefreshToken().getValue();
		
		//add refreshToken ao cookie
		addRefreshTokenInCookie(refreshToken, req, resp);
		
		//remove token do body
		removeRefreshTokenFromBody(token);
		
		return body;
	}

	/**
	 * Método responsável por remover o refreshToken do body
	 * @param token
	 * @author elton
	 */
	private void removeRefreshTokenFromBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
		
	}

	/**
	 * Método responsável por adicionar o refreshToken em um cookie 
	 * @param refreshToken
	 * @param req
	 * @param resp
	 * @author elton
	 */
	private void addRefreshTokenInCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		//Criando objeto cookie e atribuindo o nome
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		//Cookie somente será acessivel via http
		refreshTokenCookie.setHttpOnly(true);
		//Utilizar https
		refreshTokenCookie.setSecure(personalApiProperty.getSeguranca().isEnableHttps()); 
		//Define o local que o cookie será enviado pelo brownser
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		//Define o tempo em dias que o cookie vai expirar
		refreshTokenCookie.setMaxAge(2592000); // corresponde a 30 dias
		//Adiciona o cookie a resposta
		resp.addCookie(refreshTokenCookie);
	}

}
