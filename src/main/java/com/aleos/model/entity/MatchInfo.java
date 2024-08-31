package com.aleos.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class MatchInfo {

    @NonNull
    @Column(nullable = false)
    protected String format;

    @ElementCollection
    @CollectionTable(
            name = "match_history_entry",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @Column(name = "entry")
    protected List<String> historyEntries = new ArrayList<>();


    @NonNull
    @Column(nullable = false)
    protected String finalScoreRecord;  // Store scores as string like "6:3,5(4):7(6),4:6" etc.
}