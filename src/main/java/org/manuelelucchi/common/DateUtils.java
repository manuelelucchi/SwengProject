package org.manuelelucchi.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Duration sub(Date start, Date end) {
        return Duration.ofMillis(end.getTime() - start.getTime());
    }
}
