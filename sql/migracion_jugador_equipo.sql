-- Jugadores en varios equipos (uno por categoría, dorsal propio por equipo).
-- Ejecutar UNA vez en Neon (SQL Editor) ANTES de usar la API / NetBeans nuevos.
-- No borra ni modifica usuarios.equipo / usuarios.categoria / usuarios.dorsal.

CREATE TABLE IF NOT EXISTS jugador_equipo (
    id           SERIAL PRIMARY KEY,
    id_usuario   INT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    equipo_id    INT NOT NULL REFERENCES equipos(id) ON DELETE CASCADE,
    categoria_id INT NOT NULL REFERENCES categorias(id_categoria),
    dorsal       INT NOT NULL CHECK (dorsal BETWEEN 0 AND 99),
    CONSTRAINT uq_jugador_categoria UNIQUE (id_usuario, categoria_id),
    CONSTRAINT uq_equipo_dorsal     UNIQUE (equipo_id, dorsal)
);

-- Pasa lo que ya tenían los jugadores (equipo + categoría + dorsal) a la tabla nueva.
INSERT INTO jugador_equipo (id_usuario, equipo_id, categoria_id, dorsal)
SELECT u.id_usuario, e.id, c.id_categoria, u.dorsal
  FROM usuarios u
  JOIN categorias c ON c.nombre = u.categoria
  JOIN equipos e    ON e.nombre = u.equipo AND e.categoria_id = c.id_categoria
 WHERE LOWER(u.tipo_usuario) = 'usuario'
   AND u.dorsal BETWEEN 0 AND 99
ON CONFLICT DO NOTHING;

-- Revisión: jugadores que NO se pudieron pasar (equipo/categoría no coinciden o dorsal repetido).
SELECT u.id_usuario, u.nombre, u.equipo, u.categoria, u.dorsal
  FROM usuarios u
 WHERE LOWER(u.tipo_usuario) = 'usuario'
   AND NOT EXISTS (SELECT 1 FROM jugador_equipo je WHERE je.id_usuario = u.id_usuario);
