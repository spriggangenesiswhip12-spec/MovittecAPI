package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario,Integer> {

    Optional<Horario> findByGrupo(String grupo);

}