package com.roervik.timetable.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
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
                        .repeatDuration(RepeatSpec.RepeatDuration.WEEKLY)
                        .repeatPeriod(2)
                        .build())
                .build();

        final String json = mapper.writeValueAsString(event);
        final Event parsedEvent = mapper.readValue(json, Event.class);
        assertThat(parsedEvent).isEqualTo(event);
    }

    @Test
    public void testGetNextEventToday() {
        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .repeatDuration(RepeatSpec.RepeatDuration.DAILY)
                        .build())
                .build();

        final LocalDateTime currentTime = LocalDateTime.parse("2017-08-17T09:00:00");
        final LocalDateTime expectedNextStartTime = LocalDateTime.parse("2017-08-17T13:15:00");
        final LocalDateTime expectedNextEndTime = LocalDateTime.parse("2017-08-17T14:15:00");

        final Event nextEvent = event.getNextEvent(currentTime);

        assertThat(nextEvent.getStartTime()).isEqualTo(expectedNextStartTime);
        assertThat(nextEvent.getEndTime()).isEqualTo(expectedNextEndTime);
    }

    @Test
    public void testGetNextEventTomorrow() {
        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .repeatDuration(RepeatSpec.RepeatDuration.DAILY)
                        .build())
                .build();

        final LocalDateTime currentTimeLateEvening = LocalDateTime.parse("2017-08-17T21:00:00");
        final LocalDateTime expectedNextStartTimeTomorrow = LocalDateTime.parse("2017-08-18T13:15:00");
        final LocalDateTime expectedNextEndTimeTomorrow = LocalDateTime.parse("2017-08-18T14:15:00");

        final Event nextEventTomorrow = event.getNextEvent(currentTimeLateEvening);

        assertThat(nextEventTomorrow.getStartTime()).isEqualTo(expectedNextStartTimeTomorrow);
        assertThat(nextEventTomorrow.getEndTime()).isEqualTo(expectedNextEndTimeTomorrow);
    }

    @Test
    public void testGetNextEventNextWeek() {
        final LocalDateTime eventStartTime = LocalDateTime.parse("2017-08-14T13:15:00");
        final LocalDateTime eventEndTime = LocalDateTime.parse("2017-08-14T14:15:00");

        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(eventStartTime)
                .endTime(eventEndTime)
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .repeatDuration(RepeatSpec.RepeatDuration.WEEKLY)
                        .build())
                .build();

        final LocalDateTime currentTime = LocalDateTime.parse("2017-08-17T21:00:00");
        final LocalDateTime expectedNextStartTimeNextWeek = eventStartTime.plus(1, WEEKS);
        final LocalDateTime expectedNextEndTimeNextWeek = eventEndTime.plus(1, WEEKS);

        final Event nextEventNextWeek = event.getNextEvent(currentTime);

        assertThat(nextEventNextWeek.getStartTime()).isEqualTo(expectedNextStartTimeNextWeek);
        assertThat(nextEventNextWeek.getEndTime()).isEqualTo(expectedNextEndTimeNextWeek);
    }

    @Test
    public void testGetNextEventNextMonth() {
        final LocalDateTime eventStartTime = LocalDateTime.parse("2017-08-14T13:15:00");
        final LocalDateTime eventEndTime = LocalDateTime.parse("2017-08-14T14:15:00");

        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(eventStartTime)
                .endTime(eventEndTime)
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .repeatDuration(RepeatSpec.RepeatDuration.MONTHLY)
                        .build())
                .build();

        final LocalDateTime currentTime = LocalDateTime.parse("2017-08-17T21:00:00");

        final LocalDateTime expectedNextStartTimeNextMonth = eventStartTime.plus(1, MONTHS);
        final LocalDateTime expectedNextEndTimeNextMonth = eventEndTime.plus(1, MONTHS);

        final Event nextEventNextMonth = event.getNextEvent(currentTime);

        assertThat(nextEventNextMonth.getStartTime()).isEqualTo(expectedNextStartTimeNextMonth);
        assertThat(nextEventNextMonth.getEndTime()).isEqualTo(expectedNextEndTimeNextMonth);
    }

    @Test
    public void testGetNextEventNextYear() {
        final LocalDateTime eventStartTime = LocalDateTime.parse("2017-08-14T13:15:00");
        final LocalDateTime eventEndTime = LocalDateTime.parse("2017-08-14T14:15:00");

        final Event event = Event.builder()
                .id(UUID.randomUUID())
                .startTime(eventStartTime)
                .endTime(eventEndTime)
                .repeat(true)
                .repeatSpec(RepeatSpec.builder()
                        .repeatDuration(RepeatSpec.RepeatDuration.ANNUALLY)
                        .build())
                .build();

        final LocalDateTime currentTime = LocalDateTime.parse("2017-08-17T21:00:00");

        final LocalDateTime expectedNextStartTimeNextYear = eventStartTime.plus(1, YEARS);
        final LocalDateTime expectedNextEndTimeNextYear = eventEndTime.plus(1, YEARS);

        final Event nextEventNextYear = event.getNextEvent(currentTime);

        assertThat(nextEventNextYear.getStartTime()).isEqualTo(expectedNextStartTimeNextYear);
        assertThat(nextEventNextYear.getEndTime()).isEqualTo(expectedNextEndTimeNextYear);
    }
}
