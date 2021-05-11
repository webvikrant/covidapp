package in.co.itlabs.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class VolunteerFilterParams {
	private String query;

	public void clear() {
		query = null;
	}
}
