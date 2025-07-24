package dev.overlax.caloriesmanager.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class DateTimeUtils {
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static LocalDateTime atStartOfDayOrMinDate(LocalDate ld) {
        return ld != null ? ld.atStartOfDay() : MIN_DATE;
    }

    public static LocalDateTime atStartOfNextDayOrMaxDate(LocalDate ld) {
        return ld != null ? ld.plusDays(1).atStartOfDay() : MAX_DATE;
    }

    public static boolean isBetweenHalfOpen(LocalTime target, @Nullable LocalTime start, @Nullable LocalTime end) {
        return (start == null || target.compareTo(start) >= 0) && (end == null || target.compareTo(end) < 0);
    }
}
