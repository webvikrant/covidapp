package in.co.itlabs.util;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CircularFilterParams {
	private String query;
	private LocalDate fromDate;
	private LocalDate toDate;

	public void clear() {
		query = null;
		fromDate = null;
		toDate = null;
	}
}
