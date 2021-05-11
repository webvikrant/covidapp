package in.co.itlabs.util;

import in.co.itlabs.business.entities.City;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PlasmaDonorFilterParams {
	private BloodGroup bloodGroup;
	private Gender gender;
	private City city;
	private Boolean verified;
	private Boolean available;
	private String query;

	public void clear() {
		bloodGroup = null;
		gender = null;
		city = null;
		verified = null;
		available = null;
		query = null;
	}
}
