package com.example.movittecapi.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {


    private final String carpeta = "src/main/resources/static/imagenes/";


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
}