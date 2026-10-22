package com.example.movittecapi.dto;

public record PosicionResponse(
        Integer equipoId,
        String equipo,
        String logoUrl,
        int pj,
        int g,
        int e,
        int p,
        int gf,
        int gc,
        int dg,
        int pts
) {
}
