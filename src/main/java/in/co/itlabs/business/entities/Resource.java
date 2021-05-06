package in.co.itlabs.business.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

	public enum Type {
		Ambulance, Hospital_Beds, Oxygen, Plasma, Medicine, Doctor_On_Call
	}

	public enum Status {
		Not_Verified, Verified, Stale
	}

	private int id;

	private int cityId;
	private Type type;

	private String name;
	private String address;

	private String phone1;
	private String phone2;
	private String phone3;

	private String remark;

	private boolean verified;
	private Status status;

	private int createdBy;
	private LocalDateTime createdAt;

	private int updatedBy;
	private LocalDateTime updatedAt;

	// transient
	private City city;

	public String getPhones() {
		String phones = "";

		if (phone1 != null) {
			phones = phone1;
		}
		if (phone2 != null) {
			phones = phones + ", " + phone2;
		}
		if (phone3 != null) {
			phones = phones + ", " + phone3;
		}

		return phones;
	}

	public String getUpdatedAtString() {
		String string = "";

		LocalDateTime then = updatedAt.plusSeconds(0);
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