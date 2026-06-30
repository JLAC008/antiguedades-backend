package com.antiguedades.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.UUID;

@Component
public class LocalFileStorage implements FileStorage {
    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024;
    private static final long MAX_IMAGE_PIXELS = 25_000_000L;
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
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo de 2 MB");
        }
        String extension;
        try {
            extension = validatedImageExtension(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("El contenido de la imagen no es válido", e);
        }
        try {
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path dir = this.uploadDir.resolve(datePath);
            Files.createDirectories(dir);

            String filename = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), dir.resolve(filename));

            return "/uploads/" + datePath + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    private String validatedImageExtension(MultipartFile file) throws IOException {
        try (ImageInputStream imageInput = ImageIO.createImageInputStream(file.getInputStream())) {
            if (imageInput == null) {
                throw new IllegalArgumentException("El archivo no es una imagen válida");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Solo se permiten imágenes JPEG o PNG");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                String format = reader.getFormatName().toLowerCase();
                if (!format.equals("jpeg") && !format.equals("jpg") && !format.equals("png")) {
                    throw new IllegalArgumentException("Solo se permiten imágenes JPEG o PNG");
                }
                long pixels = (long) reader.getWidth(0) * reader.getHeight(0);
                if (pixels <= 0 || pixels > MAX_IMAGE_PIXELS) {
                    throw new IllegalArgumentException("La imagen tiene dimensiones no permitidas");
                }
                if (reader.read(0) == null) {
                    throw new IllegalArgumentException("El contenido de la imagen no es válido");
                }
                return format.equals("png") ? ".png" : ".jpg";
            } finally {
                reader.dispose();
            }
        }
    }

}
