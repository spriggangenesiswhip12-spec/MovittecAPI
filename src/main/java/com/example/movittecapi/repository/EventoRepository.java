package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    List<Evento> findByFechaGreaterThanEqualOrderByFechaAscHoraAsc(LocalDate fecha);
}