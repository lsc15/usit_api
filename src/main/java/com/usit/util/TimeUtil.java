package com.usit.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
	
	public static LocalDateTime getZonedDateTimeNow(String timeZone) {
		return ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDateTime();
	}
	
}
