package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import in.co.itlabs.Application;
import in.co.itlabs.business.entities.Enquiry;
import in.co.itlabs.util.EnquiryFilterParams;

public class EnquiryService {

	private static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

	private DatabaseService databaseService;

	public EnquiryService() {
		databaseService = Application.getDatabaseService();
	}

	// =================================================================================
	// enquiries
	// =================================================================================

	// create
	public int createEnquiry(List<String> messages, Enquiry resource) {

		int newEnquiryId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String insertSql = "insert into enquiry (name, phone, emailId, message, actionTaken, createdAt)"
				+ " values(:name, :phone, :emailId, :message, :actionTaken, :createdAt)";

		try (Connection con = sql2o.beginTransaction()) {
			int resourceId = con.createQuery(insertSql).addParameter("name", resource.getName())
					.addParameter("phone", resource.getPhone()).addParameter("emailId", resource.getEmailId())
					.addParameter("message", resource.getMessage())
					.addParameter("actionTaken", resource.getActionTaken())
					.addParameter("createdAt", resource.getCreatedAt()).executeUpdate().getKey(Integer.class);

			con.commit();
			newEnquiryId = resourceId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newEnquiryId;
	}

	// read many

	public int getEnquiriesCount(EnquiryFilterParams filterParams) {
		int count = 0;

		String sql = generateEnquirySql(filterParams, true);
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<Enquiry> getEnquiries(int offset, int limit, EnquiryFilterParams filterParams) {
		List<Enquiry> enquiries = null;

		String sql = generateEnquirySql(filterParams, false);
		sql = sql + " order by createdAt desc limit " + limit + " offset " + offset;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			enquiries = con.createQuery(sql).executeAndFetch(Enquiry.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return enquiries;
	}

	private String generateEnquirySql(EnquiryFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from enquiry";
		} else {
			sql = "select * from enquiry";
		}

		if (filterParams.getQuery() != null) {
			sql = sql + " where";

			int clauseCount = 0;

			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(name) like '" + queryString + "' or lower(phone) like '" + queryString + "')";
			}
		}

		return sql;
	}

	// update
	public boolean updateEnquiry(List<String> messages, Enquiry enquiry) {

		boolean success = false;
		Sql2o sql2o = databaseService.getSql2o();
		String updateSql = "update enquiry set actionTaken=:actionTaken where id=:id";

		try (Connection con = sql2o.beginTransaction()) {
			con.createQuery(updateSql).addParameter("actionTaken", enquiry.getActionTaken())
					.addParameter("id", enquiry.getId()).executeUpdate();

			con.commit();
			success = true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return success;
	}
}