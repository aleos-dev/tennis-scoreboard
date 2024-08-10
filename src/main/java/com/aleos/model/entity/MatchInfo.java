package com.aleos.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class MatchInfo {

    protected String format;

    @ElementCollection
    @CollectionTable(
            name = "match_history_entry",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @Column(name = "entry")
    protected List<String> historyEntries = new ArrayList<>();
}