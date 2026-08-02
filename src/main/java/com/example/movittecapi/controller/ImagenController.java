package com.example.movittecapi.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final Cloudinary cloudinary;

    public ImagenController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PostMapping("/subir")
    public String subirImagen(@RequestParam("archivo") MultipartFile archivo) {

        try {

            Map resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.emptyMap()
            );

            return resultado.get("secure_url").toString();

        } catch (Exception e) {

            return "ERROR: " + e.getMessage();

        }
    }
}