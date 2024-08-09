package com.aleos.model.entity;

import com.aleos.ImageService;
import com.aleos.validation.BasicGroup;
import com.aleos.validation.ExtendedGroup;
import com.aleos.validation.ValidFilePath;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "player", indexes = @Index(name = "player_name_idx", columnList = "name", unique = true))
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Player {

    @Id
    @GeneratedValue(generator = "player_seq_gen")
    @SequenceGenerator(name = "player_seq_gen", initialValue = 1000, allocationSize = 5)
    private Long id;

    @NotNull(message = "Name cannot be null.")
    @Size(min = 5, max = 50, groups = BasicGroup.class, message = "The name must be between {min} and {max}.")
    private String name;

    @NotNull(message = "Country cannot be null.")
    @Size(min = 2, max = 30, groups = BasicGroup.class, message = "The name must be between {min} and {max}.")
    private String country;

    @ValidFilePath(message = "The path to the image file is invalid.", groups = ExtendedGroup.class)
    @Column(name = "image_path")
    private String imagePath = ImageService.DEFAULT_PLAYER_IMAGE_PATH;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;
}
