package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Aviso;
import com.example.movittecapi.repository.AvisoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/avisos")
@CrossOrigin(origins = "*")
public class AvisoController {


    @Autowired
    private AvisoRepository repository;


    @GetMapping
    public List<Aviso> listar(){

        return repository.findAll();

    }
}