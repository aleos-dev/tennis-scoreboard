package com.aleos;

import com.aleos.exception.ImageServiceException;
import com.aleos.util.PropertiesUtil;
import jakarta.validation.constraints.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageService {

    private static final String AVATARS_DEFAULT_URL = PropertiesUtil.get("image.player.avatar.default.url").orElseThrow();

    private static final String AVATARS_STORAGE_PATH = PropertiesUtil.get("image.player.avatar.storage.path").orElseThrow();

    private static final String IMAGE_FORMAT = ".webp";

    static {
        // Ensure WebP plugin is registered
        ImageIO.scanForPlugins();
    }

    public void saveAvatarAsWebPImage(InputStream imageInputStream, String userId) {
        try {
            BufferedImage image = ImageIO.read(imageInputStream);
            if (image == null) {
                throw new ImageServiceException("Failed to read image from input stream.");
            }

            Path outputPath = Paths.get(AVATARS_STORAGE_PATH, userId + IMAGE_FORMAT);
            if (Files.notExists(outputPath.getParent())) {
                Files.createDirectories(outputPath.getParent());
            }

            try (var outputStream = Files.newOutputStream(outputPath)) {
                if (!ImageIO.write(image, "webp", outputStream)) {
                    throw new ImageServiceException("Failed to write image to file.");
                }
            }
        } catch (IOException e) {
            throw new ImageServiceException("Error saving the avatar for user id: %s".formatted(userId), e);
        }
    }

    public Optional<InputStream> getAvatarImageInputStream(@NotNull String name) {
        try {
            Path avatarPath = Paths.get(AVATARS_STORAGE_PATH, name + IMAGE_FORMAT);
            if (Files.exists(avatarPath)) {
                return Optional.of(Files.newInputStream(avatarPath));
            }

            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            URI defaultAvatarUrl = URI.create(AVATARS_DEFAULT_URL.formatted(encodedName));
            try (InputStream avatarStream = defaultAvatarUrl.toURL().openStream()) {
                saveAvatarAsWebPImage(avatarStream, name);
            }
            return Optional.of(defaultAvatarUrl.toURL().openStream());

        } catch (IOException e) {
            // log
            return Optional.empty();
        }
    }

    public void remove(String name) {
        try {
            Files.delete(Path.of(AVATARS_STORAGE_PATH, name + IMAGE_FORMAT));
        } catch (IOException e) {
            throw new ImageServiceException("Error removing the avatar with name: %s".formatted(name), e);
        }
    }
}

