package com.example.movittecapi.repository;

import com.example.movittecapi.entity.EventoPartido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoPartidoRepository extends JpaRepository<EventoPartido, Integer> {

    List<EventoPartido> findByPartidoIdOrderByMinutoAscIdEventoAsc(Integer partidoId);
}
