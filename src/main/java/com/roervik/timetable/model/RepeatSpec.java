package com.roervik.timetable.model;

import lombok.Builder;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
@Builder
public class RepeatSpec {
    private final RepeatDuration repeatDuration;

    @Builder.Default
    private int repeatPeriod = 1;

    public enum RepeatDuration {
        ANNUALLY("annually"),
        MONTHLY("monthly"),
        WEEKLY("weekly"),
        DAILY("daily");

        private final String name;

        RepeatDuration(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ChronoUnit getChronoUnit() {
            switch (this) {
                case DAILY: return ChronoUnit.DAYS;
                case WEEKLY: return ChronoUnit.WEEKS;
                case MONTHLY: return ChronoUnit.MONTHS;
                case ANNUALLY: return ChronoUnit.YEARS;
                default: throw new RuntimeException("Unknown duration: " + this.getName());
            }
        }
    }
}
