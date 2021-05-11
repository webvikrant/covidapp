package in.co.itlabs.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class EnquiryFilterParams {
	private String query;

	public void clear() {
		query = null;
	}
}
