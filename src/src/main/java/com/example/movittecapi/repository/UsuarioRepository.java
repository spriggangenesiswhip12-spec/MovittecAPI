package com.example.movittecapi.repository;

import com.example.movittecapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoAndActivoTrue(String correo);

    // Columnas: 0 id_usuario, 1 nombre, 2 apellido_paterno, 3 equipo, 4 categoria,
    // 5 dorsal, 6 goles, 7 asistencias. Solo usuarios activos con equipo asignado.
    @Query(value = "SELECT u.id_usuario, u.nombre, u.apellido_paterno, u.equipo, u.categoria, u.dorsal, "
            + "(SELECT COUNT(*) FROM eventos_partido ep WHERE ep.tipo = 'gol' AND ep.jugador_id = u.id_usuario), "
            + "(SELECT COUNT(*) FROM eventos_partido ep WHERE ep.tipo = 'gol' AND ep.jugador_asistencia_id = u.id_usuario) "
            + "FROM usuarios u "
            + "WHERE u.tipo_usuario = 'usuario' AND u.activo = TRUE "
            + "AND u.equipo IS NOT NULL AND u.equipo <> '' "
            + "ORDER BY 7 DESC, u.nombre",
            nativeQuery = true)
    List<Object[]> listarJugadores();
}
