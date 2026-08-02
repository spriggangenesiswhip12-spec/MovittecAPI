package com.example.movittecapi.controller;


import com.example.movittecapi.entity.Evento;
import com.example.movittecapi.repository.EventoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*")
public class EventoController {


    @Autowired
    private EventoRepository repository;



    @GetMapping
    public List<Evento> listar(){

        return repository.findAll();

    }

}