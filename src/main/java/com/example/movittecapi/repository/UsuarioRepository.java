package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByEquipoAndDorsal(String equipo, Integer dorsal);

    /**
     * Jugadores (tipo_usuario = 'usuario') con sus goles y asistencias.
     * categoria y equipo son opcionales: si vienen null no filtran.
     */
    @Query(value = """
            SELECT u.id_usuario AS id,
                   u.nombre AS nombre,
                   u.apellido_paterno AS apellido,
                   u.equipo AS equipo,
                   u.categoria AS categoria,
                   u.dorsal AS dorsal,
                   (SELECT COUNT(*) FROM eventos_partido ep
                     WHERE ep.tipo = 'gol' AND ep.jugador_id = u.id_usuario) AS goles,
                   (SELECT COUNT(*) FROM eventos_partido ep
                     WHERE ep.tipo = 'gol' AND ep.jugador_asistencia_id = u.id_usuario) AS asistencias
              FROM usuarios u
             WHERE LOWER(u.tipo_usuario) = 'usuario'
               AND u.activo = TRUE
               AND (CAST(:categoria AS text) IS NULL OR u.categoria = CAST(:categoria AS text))
               AND (CAST(:equipo AS text) IS NULL OR u.equipo = CAST(:equipo AS text))
             ORDER BY goles DESC, asistencias DESC, u.nombre ASC
            """, nativeQuery = true)
    List<JugadorStats> estadisticas(@Param("categoria") String categoria,
                                    @Param("equipo") String equipo);
}
