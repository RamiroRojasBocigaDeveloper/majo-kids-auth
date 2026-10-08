package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, Object> upload(MultipartFile multipartFile) throws IOException {
        validarArchivo(multipartFile);
        File file = convert(multipartFile);
        try {
            Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return result;
        } finally {
            Files.deleteIfExists(file.toPath());
        }
    }

    @Override
    public Map<String, Object> delete(String id) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private void validarArchivo(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen");
        }
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        if (multipartFile.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("La imagen supera el tamaño máximo de 10MB");
        }
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("upload-", ".tmp");
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(multipartFile.getBytes());
        }
        return file;
    }
}
