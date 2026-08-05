package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Horario;
import com.example.movittecapi.repository.HorarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {

    private final HorarioRepository horarioRepository;


    public HorarioController(HorarioRepository horarioRepository){
        this.horarioRepository = horarioRepository;
    }


    @GetMapping("/{grupo}")
    public Horario obtenerHorario(@PathVariable String grupo){

        return horarioRepository.findByGrupo(grupo)
                .orElse(null);
    }
}