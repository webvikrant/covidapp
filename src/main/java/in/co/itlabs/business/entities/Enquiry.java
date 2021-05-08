package in.co.itlabs.business.entities;

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
}