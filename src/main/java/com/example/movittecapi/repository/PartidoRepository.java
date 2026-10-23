package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Integer> {

    List<Partido> findAllByOrderByFechaAscHoraAsc();

    List<Partido> findByCategoriaIdOrderByFechaAscHoraAsc(Integer categoriaId);
}
