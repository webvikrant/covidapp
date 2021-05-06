package in.co.itlabs.util;

import in.co.itlabs.business.entities.City;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PlasmaDonorFilterParams {
	private City city;
	private Boolean verified;
	private Boolean available;
	private String query;

	public void clear() {
		city = null;
		verified = null;
		available = null;
		query = null;
	}
}
