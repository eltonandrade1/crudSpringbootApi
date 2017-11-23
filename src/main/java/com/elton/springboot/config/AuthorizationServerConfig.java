package com.elton.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
			.scopes("read", "write") // permissões de acesso para aplicaçao cliente (mesmo um usuário admin nessa aplicaçao somente ler e escreve) 
			.authorizedGrantTypes("password", "refresh_token") // será utilzada senha informado pelo usuario na aplicação angular e autenticação via token
			.accessTokenValiditySeconds(1800) // configura o tempo que o token fica ativo expresso em segundos
			.refreshTokenValiditySeconds(3600 * 24) // configura a validade do refresh token para 24 horas (3600seg = 1 hora * 24 = 1 dia)
			.and()
			.withClient("mobile") // cliente mobile
			.secret("m0b1l30")
			.scopes("read") // permissões de acesso para aplicaçao mobile (mesmo um usuário admin nessa aplicaçao somente ler)
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(1800)
			.refreshTokenValiditySeconds(3600 * 24);
	}		
	
	/**
	 * Metedo realiza a autenticação atraves do token e o authenticationManager
	 * 
	 * @author elton
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false) // após a expiraçao no token será gerado um novo e não reaproveitado.
			.authenticationManager(authenticationManager);
	}
	
	/**
	 * Método retorna um AccessTokenConverter configurado com a palavra chave
	 * @return AccessTokenConverter
	 * 
	 * @author elton
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter  accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("elton123"); // chave secreta utilizada
		return accessTokenConverter;
	}

	/**
	 * Método retorna o objeto TokenStore utilizando JwtAccessTokenConverter
	 * 
	 * @return TokenStore
	 * @author elton
	 * 
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
