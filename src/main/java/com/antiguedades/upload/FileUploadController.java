package com.antiguedades.upload;

import com.antiguedades.storage.FileStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private final FileStorage fileStorage;

    public FileUploadController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileStorage.store(file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al guardar el archivo"));
        }
    }
}
