package com.aleos.service;

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
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageService {

    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    private static final String AVATARS_DEFAULT_URL = PropertiesUtil.get("image.player.avatar.default.url").orElseThrow();

    private static final String AVATARS_STORAGE_PATH = PropertiesUtil.get("image.player.avatar.storage.path").orElseThrow();

    private static final Path STANDARD_AVATAR_PATH = Path.of("images/avatars/");

    private static final String IMAGE_FORMAT = ".webp";


    static {
        // Ensure the WebP plugin is registered
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
            logger.log(Level.SEVERE, "IO problems with getting input stream", e);
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

    public void deployAvatarsFromResources() {
        try {
            Path targetDirectory = Path.of(AVATARS_STORAGE_PATH);
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            String[] avatars = {"Andy Murray.webp", "Ashleigh Barty.webp", "Dominic Thiem.webp", "Maria Sharapova.webp",
                    "Naomi Osaka.webp", "Novak Djokovic.webp", "Rafael Nadal.webp", "Roger Federer.webp",
                    "Serena Williams.webp", "Simona Halep.webp", "Venus Williams.webp"};
            var classLoader = getClass().getClassLoader();

            for (var avatar : avatars) {
                try (var inputStream = classLoader.getResourceAsStream(STANDARD_AVATAR_PATH.resolve(avatar).toString())) {
                    if (inputStream != null) {
                        Path targetPath = targetDirectory.resolve(avatar);
                        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        logger.warning(() -> "Standard avatar %s not found".formatted(avatar));
                    }
                }
            }

        } catch (Exception e) {
            logger.warning("The avatars deploying exception: " + e.getMessage());
        }
    }

    public void renameAvatar(String newName, String oldName) {
        try {
            Path oldAvatarPath = Paths.get(AVATARS_STORAGE_PATH, oldName + IMAGE_FORMAT);
            Path newAvatarPath = Paths.get(AVATARS_STORAGE_PATH, newName + IMAGE_FORMAT);

            if (Files.exists(oldAvatarPath)) {
                Files.move(oldAvatarPath, newAvatarPath, StandardCopyOption.REPLACE_EXISTING);
                logger.info(() -> "Avatar renamed from " + oldName + " to " + newName);
            } else {
                getAvatarImageInputStream(newName).ifPresent(is -> saveAvatarAsWebPImage(is, newName));
            }
        } catch (IOException e) {
            throw new ImageServiceException("Error renaming the avatar from %s to %s".formatted(oldName, newName), e);
        }
    }
}

