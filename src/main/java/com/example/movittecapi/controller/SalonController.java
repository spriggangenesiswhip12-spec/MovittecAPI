package com.example.movittecapi.controller;


import com.example.movittecapi.entity.Salon;
import com.example.movittecapi.repository.SalonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/salones")
@CrossOrigin(origins = "*")
public class SalonController {


    @Autowired
    private SalonRepository repository;


    @GetMapping
    public List<Salon> listar() {

        return repository.findAll();

    }


    @GetMapping("/edificio/{id}")
    public List<Salon> buscarPorEdificio(
            @PathVariable Integer id
    ) {

        return repository.findByIdEdificioOrderByNombreAsc(id);

    }
}

