package com.roervik.timetable.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Event {
    private final UUID id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final boolean repeat;
    private final RepeatSpec repeatSpec;

    public Event getNextEvent(final LocalDateTime currentTime) {
        if (!repeat) return this;
        final TemporalUnit duration = this.repeatSpec.getRepeatDuration().getChronoUnit();
        final long durationsBetween = duration.between(startTime, currentTime);
        final long durationsToIncrement = currentTime.isBefore(startTime.plusDays(durationsBetween))
                ? durationsBetween
                : durationsBetween + 1;
        final LocalDateTime nextEventStartTime = startTime.plus(durationsToIncrement, duration);
        final LocalDateTime nextEventEndTime = endTime.plus(durationsToIncrement, duration);

        return this.toBuilder()
                .startTime(nextEventStartTime)
                .endTime(nextEventEndTime)
                .build();
    }
}
