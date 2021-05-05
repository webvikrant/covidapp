package in.co.itlabs.business.entities;

import in.co.itlabs.business.services.AuthService.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

	private int id;
	private String name;
	private String emailId;
	private String username;
	private String password;
	private String confirmPassword;
	private String passwordHash;
	private boolean enabled;
	private Role role;

	// transient

}