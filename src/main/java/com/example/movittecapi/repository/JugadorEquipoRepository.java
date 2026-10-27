package com.example.movittecapi.repository;

import com.example.movittecapi.entity.JugadorEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JugadorEquipoRepository extends JpaRepository<JugadorEquipo, Integer> {

    boolean existsByEquipoIdAndDorsal(Integer equipoId, Integer dorsal);

    boolean existsByIdUsuarioAndCategoriaId(Integer idUsuario, Integer categoriaId);

    @Query(value = """
            SELECT je.equipo_id AS "equipoId",
                   e.nombre AS "equipo",
                   je.categoria_id AS "categoriaId",
                   c.nombre AS "categoria",
                   je.dorsal AS "dorsal"
              FROM jugador_equipo je
              JOIN equipos e ON e.id = je.equipo_id
              JOIN categorias c ON c.id_categoria = je.categoria_id
             WHERE je.id_usuario = :idUsuario
             ORDER BY je.id
            """, nativeQuery = true)
    List<EquipoJugadorView> equiposDe(@Param("idUsuario") Integer idUsuario);
}
