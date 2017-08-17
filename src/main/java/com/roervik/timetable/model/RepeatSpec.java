package com.roervik.timetable.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RepeatSpec {
    private final RepeatType type;
    private final Set<Integer> repeatTimes;

    public enum RepeatType {
        WEEKLY("weekly"),
        MONTHLY("monthly"),
        DAILY("daily");

        private final String name;

        RepeatType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
