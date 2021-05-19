package in.co.itlabs.business.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Circular {

	private int id;

	private String subject;
	
	private String fileName;
	private String fileMime;
	private byte[] fileBytes;

	private int createdBy;
	private LocalDateTime createdAt;

	// transient
	private User updatedByUser;

}