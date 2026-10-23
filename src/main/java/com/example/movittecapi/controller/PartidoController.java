package com.example.movittecapi.controller;

import com.example.movittecapi.dto.EstadoRequest;
import com.example.movittecapi.dto.EventoPartidoDTO;
import com.example.movittecapi.dto.EventoRequest;
import com.example.movittecapi.dto.MarcadorRequest;
import com.example.movittecapi.dto.PartidoDTO;
import com.example.movittecapi.service.PartidoService;
import com.example.movittecapi.service.SesionService;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partidos")
@CrossOrigin(origins = "*")
public class PartidoController {

    private final PartidoService service;
    private final SesionService sesion;

    public PartidoController(PartidoService service, SesionService sesion) {
        this.service = service;
        this.sesion = sesion;
    }

    // ------------------------------------------------------------ público

    /** GET /partidos?categoria=3&estado=en_curso  (los dos filtros son opcionales) */
    @GetMapping
    public List<PartidoDTO> listar(@RequestParam(required = false) Integer categoria,
                                   @RequestParam(required = false) String estado) {
        return service.listar(categoria, estado);
    }

    @GetMapping("/{id}")
    public PartidoDTO obtener(@PathVariable Integer id) {
        return service.obtener(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(404), "Partido no encontrado"));
    }

    @GetMapping("/{id}/eventos")
    public List<EventoPartidoDTO> eventos(@PathVariable Integer id) {
        return service.eventosDe(id);
    }

    // ------------------------------------------------------------ solo árbitro / admin

    @PutMapping("/{id}/estado")
    public PartidoDTO cambiarEstado(@PathVariable Integer id,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    @RequestBody EstadoRequest body) {
        sesion.exigirArbitro(auth);
        return service.cambiarEstado(id, body.estado());
    }

    @PutMapping("/{id}/marcador")
    public PartidoDTO fijarMarcador(@PathVariable Integer id,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    @RequestBody MarcadorRequest body) {
        sesion.exigirArbitro(auth);
        return service.fijarMarcador(id, body.marcadorLocal(), body.marcadorVisitante());
    }

    @PostMapping("/{id}/eventos")
    public EventoPartidoDTO registrarEvento(@PathVariable Integer id,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            @RequestBody EventoRequest body) {
        sesion.exigirArbitro(auth);
        return service.registrarEvento(id, body);
    }

    @DeleteMapping("/{id}/eventos/{idEvento}")
    public Map<String, String> eliminarEvento(@PathVariable Integer id,
                                              @PathVariable Integer idEvento,
                                              @RequestHeader(value = "Authorization", required = false) String auth) {
        sesion.exigirArbitro(auth);
        service.eliminarEvento(id, idEvento);
        return Map.of("mensaje", "Evento eliminado");
    }
}
