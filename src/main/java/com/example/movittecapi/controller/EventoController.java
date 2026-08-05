package com.example.movittecapi.controller;

import com.example.movittecapi.entity.Evento;
import com.example.movittecapi.repository.EventoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

        LocalDateTime ahora = LocalDateTime.now();

        return eventoRepository.findAll().stream()
                .filter(evento -> {
                    LocalDateTime inicio = LocalDateTime.of(evento.getFecha(), evento.getHora());
                    LocalDateTime fin = inicio.plusHours(2); // duración automática de 2 horas

                    return fin.isAfter(ahora); // solo eventos visibles
                })
                .sorted((e1, e2) -> {
                    LocalDateTime d1 = LocalDateTime.of(e1.getFecha(), e1.getHora());
                    LocalDateTime d2 = LocalDateTime.of(e2.getFecha(), e2.getHora());
                    return d1.compareTo(d2);
                })
                .toList();
    }
}