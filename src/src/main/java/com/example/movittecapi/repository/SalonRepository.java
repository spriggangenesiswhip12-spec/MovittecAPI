package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalonRepository extends JpaRepository<Salon, Integer> {

    List<Salon> findByIdEdificioOrderByNombreAsc(Integer idEdificio);

}