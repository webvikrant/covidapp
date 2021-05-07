package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

	public enum Type {
		Ambulance, Doctor_On_Call, Hospital_Beds, Medicine, Oxygen, Plasma_BloodBank
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

}