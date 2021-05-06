package in.co.itlabs.business.entities;

import java.time.LocalDate;

import in.co.itlabs.util.BloodGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlasmaDonor {
	private int id;
	private BloodGroup bloodGroup;
	private String name;
	private Integer age;
	private String phone;
	private String pincode;
	private String address;
	private LocalDate infectionDate;
	private LocalDate recoveryDate;
	private LocalDate donationDate;
	private boolean availability;
}
