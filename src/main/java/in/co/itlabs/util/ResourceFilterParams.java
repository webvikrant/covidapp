package in.co.itlabs.util;

import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ResourceFilterParams {
	private City city;
	private Resource.Type type;
	private Resource.Status status;
	private String query;

	public void clear() {
		city = null;
		type = null;
		status = null;
		query = null;
	}
}
