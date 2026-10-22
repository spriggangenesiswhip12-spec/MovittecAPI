package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    List<Equipo> findAllByOrderByNombreAsc();

    List<Equipo> findByCategoriaIdOrderByNombreAsc(Integer categoriaId);
}
