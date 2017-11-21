package com.elton.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	/**
	 * Metodo para autenticação basica em memória, porém pode ser adaptado para
	 * consultar o login em banco de dados
	 * 
	 * @author elton
	 */
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ROLE");
	}
	
	
	/**
	 * método especifica que para qualquer requisição o usuário precisa
	 * estar autenticado
	 * 
	 * @author elton
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/categorias").permitAll() // permite acesso a qualquer usuario sem autenticação ao listar categorias
		.anyRequest().authenticated() //qualquer requisição o usuário precisa estar autenticado
//		.and().httpBasic() // tipo de autenticação basica
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //nao mantem sessão no servidor
		.and().csrf().disable(); 
	}
	
	/**
	 * Método configura a autenticação statelles
	 * 
	 * @author elton
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}

}
