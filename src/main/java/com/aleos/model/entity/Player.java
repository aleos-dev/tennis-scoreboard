package com.aleos.model.entity;

import com.aleos.validation.BasicGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

import static java.time.Instant.now;

@Entity
@Table(name = "player", indexes = @Index(name = "player_name_idx", columnList = "name", unique = true))
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Player {

    private static final String DEFAULT_COUNTRY = "XX";

    @Id
    @GeneratedValue(generator = "player_seq_gen")
    @SequenceGenerator(name = "player_seq_gen", initialValue = 1000, allocationSize = 5)
    private Long id;

    @NotNull(message = "Name cannot be null.")
    @Size(min = 5, max = 50, groups = BasicGroup.class, message = "The name must be between {min} and {max}.")
    private String name;

    @Size(min = 2, max = 2, groups = BasicGroup.class, message = "The country code must be exactly {min} characters.")
    private String country = DEFAULT_COUNTRY;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt = now();
}
