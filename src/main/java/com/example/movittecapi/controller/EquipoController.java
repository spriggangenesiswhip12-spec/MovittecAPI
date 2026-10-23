package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.repository.EquipoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoRepository repository;

    public EquipoController(EquipoRepository repository) {
        this.repository = repository;
    }

    /** GET /equipos  o  GET /equipos?categoria=3 */
    @GetMapping
    public List<Equipo> listar(@RequestParam(required = false) Integer categoria) {
        return (categoria == null)
                ? repository.findAllByOrderByNombreAsc()
                : repository.findByCategoriaIdOrderByNombreAsc(categoria);
    }
}
