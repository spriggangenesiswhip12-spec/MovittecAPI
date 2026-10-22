package com.example.movittecapi.controller;

import com.example.movittecapi.dto.EquipoResponse;
import com.example.movittecapi.entity.Categoria;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.repository.CategoriaRepository;
import com.example.movittecapi.repository.EquipoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoRepository equipoRepository;
    private final CategoriaRepository categoriaRepository;

    public EquipoController(EquipoRepository equipoRepository,
                            CategoriaRepository categoriaRepository) {
        this.equipoRepository = equipoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // GET /equipos  o  GET /equipos?categoriaId=3
    @GetMapping
    public List<EquipoResponse> listar(@RequestParam(required = false) Integer categoriaId) {
        List<Equipo> equipos = (categoriaId == null)
                ? equipoRepository.findAllByOrderByNombreAsc()
                : equipoRepository.findByCategoriaIdOrderByNombreAsc(categoriaId);

        Map<Integer, String> categorias = nombresCategorias();
        return equipos.stream().map(e -> aRespuesta(e, categorias)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponse> detalle(@PathVariable Integer id) {
        return equipoRepository.findById(id)
                .map(e -> ResponseEntity.ok(aRespuesta(e, nombresCategorias())))
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<Integer, String> nombresCategorias() {
        Map<Integer, String> mapa = new HashMap<>();
        for (Categoria c : categoriaRepository.findAll()) {
            mapa.put(c.getIdCategoria(), c.getNombre());
        }
        return mapa;
    }

    private EquipoResponse aRespuesta(Equipo e, Map<Integer, String> categorias) {
        return new EquipoResponse(
                e.getId(),
                e.getCategoriaId(),
                categorias.get(e.getCategoriaId()),
                e.getNombre(),
                e.getDescripcion(),
                e.getLogoUrl()
        );
    }
}
