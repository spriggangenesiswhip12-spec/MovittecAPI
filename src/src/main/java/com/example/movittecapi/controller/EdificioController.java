package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Edificio;
import com.example.movittecapi.repository.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/edificios")
@CrossOrigin(origins = "*")
public class EdificioController {

    @Autowired
    private EdificioRepository repository;

    @GetMapping
    public List<Edificio> listar() {
        return repository.findAll();
    }
}