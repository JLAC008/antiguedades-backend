package com.antiguedades.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalFileStorageTest {

    @TempDir
    Path uploadDir;

    @Test
    void storesValidatedPngWithServerControlledExtension() throws Exception {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bytes);
        MockMultipartFile file = new MockMultipartFile(
            "file", "payload.exe", "application/octet-stream", bytes.toByteArray());

        String url = new LocalFileStorage(uploadDir.toString()).store(file);

        assertThat(url).endsWith(".png");
        assertThat(Files.exists(uploadDir.resolve(url.substring("/uploads/".length())))).isTrue();
    }

    @Test
    void rejectsNonImageEvenWhenFilenameClaimsJpeg() {
        MockMultipartFile file = new MockMultipartFile(
            "file", "malware.jpg", "image/jpeg", "not an image".getBytes());

        assertThatThrownBy(() -> new LocalFileStorage(uploadDir.toString()).store(file))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
