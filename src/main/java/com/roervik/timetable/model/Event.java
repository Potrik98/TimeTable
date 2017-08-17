package com.roervik.timetable.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

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
        if (repeatSpec.getType() == RepeatSpec.RepeatType.DAILY) {
            final long daysBetween = DAYS.between(startTime, currentTime);
            final long daysToIncrement = currentTime.isBefore(startTime.plusDays(daysBetween))
                    ? daysBetween
                    : daysBetween + 1;
            final LocalDateTime nextEventStartTime = startTime.plusDays(daysToIncrement);
            final LocalDateTime nextEventEndTime = endTime.plusDays(daysToIncrement);
            return this.toBuilder()
                    .startTime(nextEventStartTime)
                    .endTime(nextEventEndTime)
                    .build();
        }
        return null;
    }
}
