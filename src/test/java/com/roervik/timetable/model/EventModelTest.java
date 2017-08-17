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
        final Event event = Event.builder()
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

        final String json = mapper.writeValueAsString(event);
        final Event parsedEvent = mapper.readValue(json, Event.class);
        System.out.println(json);
        assertThat(parsedEvent).isEqualTo(event);
    }

    @Test
    public void testGetNextEventDaily() {
        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .type(RepeatSpec.RepeatType.DAILY)
                        .build())
                .build();

        final LocalDateTime currentTime = LocalDateTime.parse("2017-08-17T09:00:00");
        final LocalDateTime expectedNextStartTime = LocalDateTime.parse("2017-08-17T13:15:00");
        final LocalDateTime expectedNextEndTime = LocalDateTime.parse("2017-08-17T14:15:00");

        final Event nextEvent = event.getNextEvent(currentTime);

        assertThat(nextEvent.getStartTime()).isEqualTo(expectedNextStartTime);
        assertThat(nextEvent.getEndTime()).isEqualTo(expectedNextEndTime);

        final LocalDateTime currentTimeLateEvening = LocalDateTime.parse("2017-08-17T21:00:00");
        final LocalDateTime expectedNextStartTimeTomorrow = LocalDateTime.parse("2017-08-18T13:15:00");
        final LocalDateTime expectedNextEndTimeTomorrow = LocalDateTime.parse("2017-08-18T14:15:00");

        final Event nextEventTomorrow = event.getNextEvent(currentTimeLateEvening);

        assertThat(nextEventTomorrow.getStartTime()).isEqualTo(expectedNextStartTimeTomorrow);
        assertThat(nextEventTomorrow.getEndTime()).isEqualTo(expectedNextEndTimeTomorrow);
    }
}
