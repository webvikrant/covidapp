package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import in.co.itlabs.Application;
import in.co.itlabs.business.entities.User;
import lombok.Data;
import lombok.Getter;

public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private Argon2 argon2 = Argon2Factory.create();

	public enum Role {
		Admin, Verifier
	}

	@Getter
	public final class AuthenticatedUser {
		private final int id;
		private final String name;
		private final Role role;

		private AuthenticatedUser(int id, String name, Role role) {
			this.id = id;
			this.name = name;
			this.role = role;
		}
	}

	@Data
	public static final class Credentials {
		private String username;
		private String password;
	}

	private DatabaseService databaseService;

	public AuthService() {
		databaseService = Application.getDatabaseService();
	}
	// =================================================================================
	// users
	// =================================================================================

	// create
	public int createUser(List<String> messages, User user) {

		int newUserId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String insertUserSql = "insert into user (name, emailId, username, passwordHash, enabled)"
				+ " values(:name,:emailId, :username, :passwordHash, :enabled)";

		char[] passwordChars = user.getPassword().toCharArray();
		String passwordHash = argon2.hash(4, 16 * 1024, 1, passwordChars);

		try (Connection con = sql2o.beginTransaction()) {
			int userId = con.createQuery(insertUserSql).addParameter("name", user.getName())
					.addParameter("emailId", user.getEmailId()).addParameter("username", user.getUsername())
					.addParameter("passwordHash", passwordHash).addParameter("enabled", false).executeUpdate()
					.getKey(Integer.class);

			con.commit();
			newUserId = userId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newUserId;
	}

	// read many
	public List<User> getAllUsers(String queryString) {
		List<User> users = null;

		if (queryString == null) {
			queryString = "";
		}

		queryString = "%" + queryString.toLowerCase() + "%";
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from user where lower(name) like :name or lower(username) like :username";
			users = con.createQuery(sql).addParameter("name", queryString).addParameter("username", queryString)
					.executeAndFetch(User.class);
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return users;
	}

	public User getUserById(int id) {
		User user = null;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from user where id = :id";
			user = con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(User.class);
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return user;
	}

	public AuthenticatedUser authenticate(List<String> messages, Credentials credentials) {
		AuthenticatedUser authUser = null;
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from user where username = :username";
			User user = con.createQuery(sql).addParameter("username", credentials.username)
					.executeAndFetchFirst(User.class);
			if (user != null) {

				// check if passwordHash is null or blank
				if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
					if (credentials.password == null || credentials.password.isEmpty()) {
						authUser = new AuthenticatedUser(user.getId(), user.getName(), user.getRole());
					}
				} else {
					char[] passwordChars = credentials.password.toCharArray();

					boolean success = argon2.verify(user.getPasswordHash(), passwordChars);

					if (success) {
						authUser = new AuthenticatedUser(user.getId(), user.getName(), user.getRole());
					}else {
						messages.add("Invalid username or password");
					}
				}
			}else {
				messages.add("Invalid username or password");
			}
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.out.println(e.getMessage());
			messages.add(e.getMessage());
		}

		return authUser;
	}

	// update password
	public boolean updateUserPassword(List<String> messages, AuthenticatedUser authUser, String password) {

		boolean success = false;
		Sql2o sql2o = databaseService.getSql2o();
		String updateSql = "update user set passwordHash = :passwordHash where id = :id";

		char[] passwordChars = password.toCharArray();
		String passwordHash = argon2.hash(4, 16 * 1024, 1, passwordChars);

		try (Connection con = sql2o.beginTransaction()) {
			con.createQuery(updateSql).addParameter("passwordHash", passwordHash).addParameter("id", authUser.getId())
					.executeUpdate().getKey(Integer.class);

			con.commit();
			success = true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return success;
	}

}