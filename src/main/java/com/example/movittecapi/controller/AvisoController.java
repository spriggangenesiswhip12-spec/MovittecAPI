package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Aviso;
import com.example.movittecapi.repository.AvisoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisos")
@CrossOrigin(origins = "*")
public class AvisoController {

    private final AvisoRepository avisoRepository;

    public AvisoController(AvisoRepository avisoRepository) {
        this.avisoRepository = avisoRepository;
    }

    @GetMapping
    public List<Aviso> obtenerAvisos() {
        return avisoRepository.findAllByOrderByFechaDesc();
    }
}