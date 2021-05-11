package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

	public enum Type {
		Ambulance, Doctor_On_Call, Hospital_Beds, Home_Nursing_and_ICU, Medicine, Oxygen, Plasma_BloodBank,
		Food_Services, Funeral_Services
	}

	public enum Status {
		Pending, Unreachable, Verified, Scam
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

	private Status status;

	private int createdBy;
	private LocalDateTime createdAt;

	private int updatedBy;
	private LocalDateTime updatedAt;

	private String guestName;
	private String guestPhone;

	// transient
	private City city;
	private User updatedByUser;

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