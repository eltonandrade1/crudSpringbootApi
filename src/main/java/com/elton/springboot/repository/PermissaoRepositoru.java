package com.elton.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Permissao;

public interface PermissaoRepositoru extends JpaRepository<Permissao, Long> {

}
