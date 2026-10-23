package com.example.movittecapi.service;

import com.example.movittecapi.entity.Usuario;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tokens simples en memoria para proteger las acciones de árbitro.
 * Si Render reinicia el servidor los tokens se pierden y el árbitro
 * solo tiene que iniciar sesión otra vez.
 */
@Service
public class SesionService {

    private final Map<String, Integer> tokens = new ConcurrentHashMap<>();
    private final UsuarioRepository usuarios;

    public SesionService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public String crearToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, usuario.getIdUsuario());
        return token;
    }

    /** Devuelve el usuario si el token es válido y es árbitro o admin; si no, lanza 401/403. */
    public Usuario exigirArbitro(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Inicia sesión para continuar");
        }

        Integer id = tokens.get(authorization.substring(7).trim());
        if (id == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401),
                    "Tu sesión expiró, vuelve a iniciar sesión");
        }

        Usuario usuario = usuarios.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(401), "Usuario no encontrado"));

        String tipo = usuario.getTipoUsuario() == null ? "" : usuario.getTipoUsuario().toLowerCase();
        boolean permitido = tipo.equals("arbitro") || tipo.equals("admin");

        if (!Boolean.TRUE.equals(usuario.getActivo()) || !permitido) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "No tienes permiso para esta acción");
        }
        return usuario;
    }
}
