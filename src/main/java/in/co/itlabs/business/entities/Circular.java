package in.co.itlabs.business.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Circular {

	private int id;

	private LocalDate date;
	private String subject;

	private String fileName;
	private String fileMime;
	private byte[] fileBytes;

	private int createdBy;
	private LocalDateTime createdAt;

	// transient
	private User createdByUser;

	public boolean isImage() {
		boolean isImage = false;
		if (fileMime != null) {
			if (fileMime.equalsIgnoreCase("image/jpeg") || fileMime.equalsIgnoreCase("image/png")) {
				isImage = true;
			}
		}
		return isImage;
	}
}