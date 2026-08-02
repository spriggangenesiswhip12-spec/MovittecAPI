package com.example.movittecapi.controller;

import com.example.movittecapi.dto.LoginRequest;
import com.example.movittecapi.dto.LoginResponse;
import com.example.movittecapi.dto.RegistroRequest;
import com.example.movittecapi.entity.Usuario;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {

        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("mensaje", "El correo ya existe"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setMatricula(request.getMatricula());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(request.getContrasena());
        usuario.setGrupo(request.getGrupo());
        usuario.setGrupoIngles(request.getGrupoIngles());
        usuario.setTipoUsuario("ALUMNO");
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok()
                .body(java.util.Map.of("mensaje", "Usuario registrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Correo incorrecto");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContrasena().equals(request.getContrasena())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        LoginResponse response = new LoginResponse(

                usuario.getIdUsuario(),

                usuario.getNombre(),

                usuario.getApellidoPaterno(),

                usuario.getApellidoMaterno(),

                usuario.getMatricula(),

                usuario.getCorreo(),

                usuario.getTipoUsuario(),

                usuario.getFotoPerfil(),

                usuario.getGrupo(),

                usuario.getGrupoIngles()

        );

        return ResponseEntity.ok(response);
    }
}