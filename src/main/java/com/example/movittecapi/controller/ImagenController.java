package com.example.movittecapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    // Carpeta REAL en Render
    private final String carpeta =
            System.getProperty("user.dir") + "/imagenes/";


    @PostMapping("/subir")
    public String subirImagen(@RequestParam("archivo") MultipartFile archivo) {

        try {

            File directorio = new File(carpeta);

            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            Path ruta = Paths.get(
                    carpeta + archivo.getOriginalFilename()
            );

            Files.write(
                    ruta,
                    archivo.getBytes()
            );

            return archivo.getOriginalFilename();

        } catch (Exception e) {

            return "ERROR: " + e.getMessage();
        }
    }


    @GetMapping("/{nombre}")
    public ResponseEntity<Resource> obtenerImagen(
            @PathVariable String nombre
    ) {

        try {

            Path ruta = Paths.get(carpeta + nombre);

            Resource recurso = new UrlResource(ruta.toUri());

            if (recurso.exists()) {

                return ResponseEntity.ok(recurso);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }
}