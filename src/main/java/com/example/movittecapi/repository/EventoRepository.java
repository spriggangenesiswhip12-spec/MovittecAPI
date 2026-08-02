package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepository extends JpaRepository<Evento, Integer> {

}