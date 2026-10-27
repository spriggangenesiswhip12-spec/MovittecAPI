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
     * Jugadores (tipo_usuario = 'usuario') con sus goles y asistencias, UNA FILA POR CADA EQUIPO
     * en el que juegan (tabla jugador_equipo); los goles se cuentan solo con el equipo de esa fila.
     * categoria y equipo son opcionales: si vienen null no filtran.
     */
    @Query(value = """
            SELECT u.id_usuario AS id,
                   u.nombre AS nombre,
                   u.apellido_paterno AS apellido,
                   e.nombre AS equipo,
                   c.nombre AS categoria,
                   je.dorsal AS dorsal,
                   (SELECT COUNT(*) FROM eventos_partido ep
                     WHERE ep.tipo = 'gol' AND ep.jugador_id = u.id_usuario
                       AND ep.equipo_id = je.equipo_id) AS goles,
                   (SELECT COUNT(*) FROM eventos_partido ep
                     WHERE ep.tipo = 'gol' AND ep.jugador_asistencia_id = u.id_usuario
                       AND ep.equipo_id = je.equipo_id) AS asistencias
              FROM jugador_equipo je
              JOIN usuarios u ON u.id_usuario = je.id_usuario
              JOIN equipos e ON e.id = je.equipo_id
              JOIN categorias c ON c.id_categoria = je.categoria_id
             WHERE LOWER(u.tipo_usuario) = 'usuario'
               AND u.activo = TRUE
               AND (CAST(:categoria AS text) IS NULL OR c.nombre = CAST(:categoria AS text))
               AND (CAST(:equipo AS text) IS NULL OR e.nombre = CAST(:equipo AS text))
             ORDER BY goles DESC, asistencias DESC, u.nombre ASC
            """, nativeQuery = true)
    List<JugadorStats> estadisticas(@Param("categoria") String categoria,
                                    @Param("equipo") String equipo);
}
