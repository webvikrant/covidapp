package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Volunteer {

	public enum Service {
		TeleCalling_DataVerification, DataEntry, Other
	}

	private int id;
	private String name;
	private Integer age;
	private String phone;
	private Integer hours;
	private Service service;
	private String otherService;
	private LocalDateTime createdAt;
}
