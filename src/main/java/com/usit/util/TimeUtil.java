package com.usit.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
	
	public static LocalDateTime getZonedDateTimeNow(String timeZone) {
		return ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDateTime();
	}
	
	public static LocalDateTime getStringToDateTime(String yyyymmddhhmm) {
		int year = Integer.parseInt(yyyymmddhhmm.substring(0, 4));
		int month = Integer.parseInt(yyyymmddhhmm.substring(4, 6));
		int day = Integer.parseInt(yyyymmddhhmm.substring(6, 8));
		int hour = Integer.parseInt(yyyymmddhhmm.substring(8, 10));
		int minute = Integer.parseInt(yyyymmddhhmm.substring(10, 12));
		
		
		return LocalDateTime.of(year, month, day, hour, minute);
	}

}
