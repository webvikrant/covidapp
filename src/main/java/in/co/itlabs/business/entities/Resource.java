package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

	public enum Type {
		Ambulance, Hospital_Beds, Oxygen, Plasma_Donor, Medicine, Doctor_On_Call
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
}