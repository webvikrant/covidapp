package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Enquiry {
	private int id;
	private String name;
	private String phone;
	private String emailId;
	private String message;
	private Boolean actionTaken;
	private LocalDateTime createdAt;
}
