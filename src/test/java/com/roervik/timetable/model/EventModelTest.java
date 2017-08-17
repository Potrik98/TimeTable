package com.roervik.timetable.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventModelTest {
    private static final String startTime = "2017-01-01T13:15:00";
    private static final String endTime = "2017-01-01T14:15:00";

    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    public void testWriteModelToJson() throws IOException {
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .type(RepeatSpec.RepeatType.WEEKLY)
                        .repeatTimes(ImmutableSet.of(
                                DayOfWeek.MONDAY.getValue(),
                                DayOfWeek.TUESDAY.getValue(),
                                DayOfWeek.FRIDAY.getValue()
                        ))
                        .build())
                .build();

        String json = mapper.writeValueAsString(event);
        Event parsedEvent = mapper.readValue(json, Event.class);
        assertThat(parsedEvent).isEqualTo(event);
    }
}
