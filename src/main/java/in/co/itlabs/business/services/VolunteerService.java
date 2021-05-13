package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import in.co.itlabs.Application;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.Volunteer;
import in.co.itlabs.util.VolunteerFilterParams;

public class VolunteerService {

	private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

	private DatabaseService databaseService;

	public VolunteerService() {
		databaseService = Application.getDatabaseService();
	}

	// =================================================================================
	// volunteers
	// =================================================================================

	// create
	public int createVolunteer(List<String> messages, Volunteer volunteer) {

		int newVolunteerId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String insertSql = "insert into volunteer (name, age, phone, emailId, hours, service, otherService, createdAt)"
				+ " values(:name, :age, :phone, :emailId, :hours, :service, :otherService, :createdAt)";

		try (Connection con = sql2o.beginTransaction()) {
			int volunteerId = con.createQuery(insertSql).addParameter("name", volunteer.getName())
					.addParameter("age", volunteer.getAge()).addParameter("phone", volunteer.getPhone())
					.addParameter("emailId", volunteer.getEmailId()).addParameter("hours", volunteer.getHours())
					.addParameter("service", volunteer.getService())
					.addParameter("otherService", volunteer.getOtherService())
					.addParameter("createdAt", volunteer.getCreatedAt()).executeUpdate().getKey(Integer.class);

			con.commit();
			newVolunteerId = volunteerId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newVolunteerId;
	}

	// read many

	public int getVolunteersCount(VolunteerFilterParams filterParams) {
		int count = 0;

		String sql = generateVolunteerSql(filterParams, true);
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<Volunteer> getVolunteers(int offset, int limit, VolunteerFilterParams filterParams) {
		List<Volunteer> volunteers = null;

		String sql = generateVolunteerSql(filterParams, false);
		sql = sql + " order by createdAt desc limit " + limit + " offset " + offset;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			volunteers = con.createQuery(sql).executeAndFetch(Volunteer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return volunteers;
	}

	private String generateVolunteerSql(VolunteerFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from volunteer";
		} else {
			sql = "select * from volunteer";
		}

		if (filterParams.getQuery() != null) {
			sql = sql + " where";

			int clauseCount = 0;

			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(name) like '" + queryString + "' or lower(phone) like '" + queryString
						+ "' or lower(emailId) like '" + queryString + "')";
			}
		}

		return sql;
	}

	// update
	public boolean updateVolunteer(List<String> messages, Resource resource) {

		boolean success = false;
		Sql2o sql2o = databaseService.getSql2o();
		String insertSql = "update resource set cityId=:cityId, type=:type, name=:name, address=:address,"
				+ " phone1=:phone1, phone2=:phone2, phone3=:phone3, remark=:remark, status=:status,"
				+ " createdBy=:createdBy, createdAt=:createdAt, updatedBy=:updatedBy, updatedAt=:updatedAt"
				+ " where id=:id";

		try (Connection con = sql2o.beginTransaction()) {
			con.createQuery(insertSql).addParameter("cityId", resource.getCityId())
					.addParameter("type", resource.getType()).addParameter("name", resource.getName())
					.addParameter("address", resource.getAddress()).addParameter("phone1", resource.getPhone1())
					.addParameter("phone2", resource.getPhone2()).addParameter("phone3", resource.getPhone3())
					.addParameter("remark", resource.getRemark()).addParameter("status", resource.getStatus())
					.addParameter("createdBy", resource.getCreatedBy())
					.addParameter("createdAt", resource.getCreatedAt())
					.addParameter("updatedBy", resource.getUpdatedBy())
					.addParameter("updatedAt", resource.getUpdatedAt()).addParameter("id", resource.getId())
					.executeUpdate();

			con.commit();
			success = true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return success;
	}
}