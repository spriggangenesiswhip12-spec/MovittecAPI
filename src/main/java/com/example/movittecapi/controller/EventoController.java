package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Evento;
import com.example.movittecapi.repository.EventoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoRepository eventoRepository;

    public EventoController(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @GetMapping
    public List<Evento> obtenerEventos() {

        return eventoRepository.findAll().stream()
                .filter(e -> {
                    LocalDateTime inicio = LocalDateTime.of(e.getFecha(), e.getHora());
                    LocalDateTime fin = inicio.plusHours(1);

                    return LocalDateTime.now().isBefore(fin);
                })
                .sorted(Comparator.comparing(Evento::getFecha)
                        .thenComparing(Evento::getHora))
                .toList();
    }
}