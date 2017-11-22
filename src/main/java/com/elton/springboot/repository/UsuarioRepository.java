package com.elton.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	// Optional dispensa o teste para null
	public Optional<Usuario> findByEmail(String email);

}
