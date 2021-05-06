package in.co.itlabs.business.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import in.co.itlabs.util.BloodGroup;
import in.co.itlabs.util.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlasmaDonor {

	private int id;
	private BloodGroup bloodGroup;
	private Gender gender;
	private String name;
	private Integer age;
	private String phone;
	private String pincode;
	private String address;
	private LocalDate infectionDate;
	private LocalDate recoveryDate;
	private boolean available;
	private boolean verified;
	private String remark;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
