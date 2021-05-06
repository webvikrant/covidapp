package in.co.itlabs.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {
	public static String humanize(LocalDateTime dateTime) {
		String string = "";

		LocalDateTime then = dateTime.plusSeconds(0);
		LocalDateTime now = LocalDateTime.now();

		long years = then.until(now, ChronoUnit.YEARS);
		then = then.plusYears(years);

		long months = then.until(now, ChronoUnit.MONTHS);
		then = then.plusMonths(months);

		long days = then.until(now, ChronoUnit.DAYS);
		then = then.plusDays(days);

		long hours = then.until(now, ChronoUnit.HOURS);
		then = then.plusHours(hours);

		long minutes = then.until(now, ChronoUnit.MINUTES);
		then = then.plusMinutes(minutes);

		if (years > 0) {
			string = string + years + "yrs";
		}

		if (months > 0) {
			string = string + "  " + months + "mths";
		}

		if (days > 0) {
			string = string + "  " + days + "days";
		}

		if (hours > 0) {
			string = string + "  " + hours + "hrs";
		}

		if (minutes > 0) {
			string = string + "  " + minutes + "mins";
		}

		if(!string.isEmpty()) {
			string = string + " ago";	
		}else {
			string = "Just now";
		}

		return string;
	}
}
