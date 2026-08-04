package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Integer> {

    List<Aviso> findAllByOrderByFechaDesc();
}