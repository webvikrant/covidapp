package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {

	public enum Type {
		Ambulance, Hospital_Beds, Oxygen, Plasma, Medicine, Doctor_On_Call
	}

	private int id;

	private int cityId;
	private Type type;

	private String name;
	private String address;

	private String phone1;
	private String phone2;
	private String phone3;
	private String phone4;
	private String phone5;

	private String remark;

	private int createdBy;
	private LocalDateTime createdAt;
	
	private int verifiedBy;
	private LocalDateTime verifiedAt;

	// transient
	private City city;
}