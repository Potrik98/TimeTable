package com.roervik.timetable.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Event {
    private final UUID id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final boolean repeat;
    private final RepeatSpec repeatSpec;
}
