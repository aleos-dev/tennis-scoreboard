package com.aleos.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "match", indexes = {
        @Index(name = "match_player1_idx", columnList = "player1_id"),
        @Index(name = "match_player2_idx", columnList = "player2_id"),
        @Index(name = "concluded_at_idx", columnList = "concluded_at", unique = true),
})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Match {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1_id")
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2_id")
    private Player playerTwo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winner_id")
    private Player winner;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant concludedAt;

    @Embedded
    private MatchInfo info;
}
