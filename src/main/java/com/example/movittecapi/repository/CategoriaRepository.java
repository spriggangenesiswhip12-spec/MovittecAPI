package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findAllByOrderByNombreAsc();

    boolean existsByNombre(String nombre);
}
