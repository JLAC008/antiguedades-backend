package com.antiguedades.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class LocalFileStorage implements FileStorage {
    private final Path uploadDir;

    public LocalFileStorage(@Value("${app.upload.dir}") String uploadDirPath) {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads: " + this.uploadDir, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo de 2 MB");
        }
        try {
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path dir = this.uploadDir.resolve(datePath);
            Files.createDirectories(dir);

            String ext = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + ext;
            Files.copy(file.getInputStream(), dir.resolve(filename));

            return "/uploads/" + datePath + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

}
