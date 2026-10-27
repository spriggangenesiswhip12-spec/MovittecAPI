package com.example.movittecapi.controller;

import com.example.movittecapi.dto.EquipoJugadorDTO;
import com.example.movittecapi.dto.LoginRequest;
import com.example.movittecapi.dto.LoginResponse;
import com.example.movittecapi.dto.RegistroRequest;
import com.example.movittecapi.entity.Categoria;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.entity.JugadorEquipo;
import com.example.movittecapi.entity.Usuario;
import com.example.movittecapi.repository.CategoriaRepository;
import com.example.movittecapi.repository.EquipoRepository;
import com.example.movittecapi.repository.JugadorEquipoRepository;
import com.example.movittecapi.repository.UsuarioRepository;
import com.example.movittecapi.service.SesionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SesionService sesionService;
    private final JugadorEquipoRepository jugadorEquipoRepository;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             EquipoRepository equipoRepository,
                             CategoriaRepository categoriaRepository,
                             SesionService sesionService,
                             JugadorEquipoRepository jugadorEquipoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
        this.categoriaRepository = categoriaRepository;
        this.sesionService = sesionService;
        this.jugadorEquipoRepository = jugadorEquipoRepository;
    }

    private static boolean vacio(String s) {
        return s == null || s.isBlank();
    }

    private static ResponseEntity<?> mensaje(int codigo, String texto) {
        return ResponseEntity.status(codigo).body(Map.of("mensaje", texto));
    }

    @Transactional
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest r) {

        if (vacio(r.nombre()) || vacio(r.apellidoPaterno()) || vacio(r.correo())
                || vacio(r.contrasena()) || vacio(r.equipo()) || vacio(r.categoria())
                || r.dorsal() == null) {
            return mensaje(400, "Completa todos los campos");
        }
        if (r.dorsal() < 0 || r.dorsal() > 99) {
            return mensaje(400, "El dorsal debe estar entre 0 y 99");
        }

        String correo = r.correo().trim();
        String equipo = r.equipo().trim();
        String categoria = r.categoria().trim();

        if (usuarioRepository.findByCorreoIgnoreCase(correo).isPresent()) {
            return mensaje(400, "El correo ya existe");
        }
        if (!equipoRepository.existsByNombre(equipo)) {
            return mensaje(400, "El equipo no existe");
        }
        Categoria categoriaDb = categoriaRepository.findFirstByNombre(categoria).orElse(null);
        if (categoriaDb == null) {
            return mensaje(400, "La categoría no existe");
        }
        Equipo equipoDb = equipoRepository
                .findFirstByNombreAndCategoriaId(equipo, categoriaDb.getIdCategoria()).orElse(null);
        if (equipoDb == null) {
            return mensaje(400, "Ese equipo no pertenece a esa categoría");
        }
        if (jugadorEquipoRepository.existsByEquipoIdAndDorsal(equipoDb.getId(), r.dorsal())) {
            return mensaje(400, "Ese dorsal ya está ocupado en tu equipo");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(r.nombre().trim());
        usuario.setApellidoPaterno(r.apellidoPaterno().trim());
        usuario.setApellidoMaterno(vacio(r.apellidoMaterno()) ? null : r.apellidoMaterno().trim());
        usuario.setCorreo(correo);
        usuario.setContrasena(r.contrasena());
        usuario.setTipoUsuario("usuario");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEquipo(equipo);
        usuario.setCategoria(categoria);
        usuario.setDorsal(r.dorsal());

        usuarioRepository.save(usuario);

        jugadorEquipoRepository.save(new JugadorEquipo(
                usuario.getIdUsuario(), equipoDb.getId(), categoriaDb.getIdCategoria(), r.dorsal()));

        return ResponseEntity.ok().body(Map.of("mensaje", "Usuario registrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (vacio(request.correo()) || vacio(request.contrasena())) {
            return mensaje(400, "Completa todos los campos");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoIgnoreCase(request.correo().trim());

        if (usuarioOpt.isEmpty()) {
            return mensaje(401, "Correo incorrecto");
        }

        Usuario usuario = usuarioOpt.get();

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            return mensaje(403, "Usuario desactivado");
        }

        if (usuario.getContrasena() == null || !usuario.getContrasena().equals(request.contrasena())) {
            return mensaje(401, "Contraseña incorrecta");
        }

        String tipo = usuario.getTipoUsuario() == null ? "" : usuario.getTipoUsuario().toLowerCase();
        String token = (tipo.equals("arbitro") || tipo.equals("admin"))
                ? sesionService.crearToken(usuario)
                : null;

        List<EquipoJugadorDTO> equipos = EquipoJugadorDTO.de(
                jugadorEquipoRepository.equiposDe(usuario.getIdUsuario()));
        EquipoJugadorDTO principal = equipos.isEmpty() ? null : equipos.get(0);

        LoginResponse response = new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno(),
                usuario.getCorreo(),
                tipo,
                usuario.getFotoPerfil(),
                principal == null ? null : principal.equipo(),
                principal == null ? null : principal.categoria(),
                principal == null ? null : principal.dorsal(),
                token,
                equipos
        );

        return ResponseEntity.ok(response);
    }
}
