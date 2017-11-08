package com.elton.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * Metodo define o cliente que tera acesso a aplicação na autenticação Oauth2
	 * 
	 * @author elton
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() // armazena em memória, mas poderia ser banco
			.withClient("angular") // define um cliente angular (login)
			.secret("@angular@") // senha de acesso do cliente
			.scopes("read", "write") // permissões de acesso 
			.authorizedGrantTypes("password") // será utilzada senha informado pelo usuario na aplicação angular
			.accessTokenValiditySeconds(1800); // configura o tempo que o token fica ativo expresso em segundos (1800seg = 30min)
	}
	
	/**
	 * Metedo realiza a autenticação atraves do token e o authenticationManager
	 * 
	 * @author elton
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
	}
	
	/**
	 * Método retorna o objeto TokenStore e armazena em memória, onde também 
	 * poderia ser armazenado em banco
	 * 
	 * @return TokenStore
	 * @author elton
	 * 
	 */
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

}
