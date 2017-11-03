package com.elton.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elton.springboot.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
