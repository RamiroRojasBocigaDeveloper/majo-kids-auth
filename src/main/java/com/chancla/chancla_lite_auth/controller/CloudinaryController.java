package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
@Tag(name = "Cloudinary", description = "Endpoints para la gestión de imágenes con Cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(summary = "Subir imagen", description = "Sube una imagen a Cloudinary y devuelve los datos de la subida, incluyendo la URL.")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(cloudinaryService.upload(file), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar imagen", description = "Elimina una imagen de Cloudinary por su ID público.")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("id") String id) throws IOException {
        return new ResponseEntity<>(cloudinaryService.delete(id), HttpStatus.OK);
    }
}
