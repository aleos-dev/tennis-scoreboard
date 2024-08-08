package com.aleos.model;

import com.aleos.model.enums.MatchStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class MatchInfo {

    @Enumerated(EnumType.STRING)
    protected MatchStatus status;

    protected String format;

    protected MatchHistory history;
}