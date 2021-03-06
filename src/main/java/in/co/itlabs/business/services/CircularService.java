package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import in.co.itlabs.Application;
import in.co.itlabs.business.entities.Circular;
import in.co.itlabs.util.CircularFilterParams;

public class CircularService {

	private static final Logger logger = LoggerFactory.getLogger(CircularService.class);

	private DatabaseService databaseService;

	public CircularService() {
		databaseService = Application.getDatabaseService();
	}

	// =================================================================================
	// enquiries
	// =================================================================================

	// create
	public int createCircular(List<String> messages, Circular circular) {

		int newCircularId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String sql = "insert into circular (date, subject, fileName, fileMime, fileBytes, createdBy, createdAt)"
				+ " values(:date, :subject, :fileName, :fileMime, :fileBytes, :createdBy, :createdAt)";

		try (Connection con = sql2o.beginTransaction()) {
			int circularId = con.createQuery(sql).addParameter("date", circular.getDate())
					.addParameter("subject", circular.getSubject()).addParameter("fileName", circular.getFileName())
					.addParameter("fileMime", circular.getFileMime()).addParameter("fileBytes", circular.getFileBytes())
					.addParameter("createdBy", circular.getCreatedBy())
					.addParameter("createdAt", circular.getCreatedAt()).executeUpdate().getKey(Integer.class);

			con.commit();
			newCircularId = circularId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newCircularId;
	}

	// read many

	public int getCircularsCount(CircularFilterParams filterParams) {
		int count = 0;

		String sql = generateCircularSql(filterParams, true);
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<Circular> getCirculars(int offset, int limit, CircularFilterParams filterParams) {
		List<Circular> circulars = null;

		String sql = generateCircularSql(filterParams, false);
		sql = sql + " order by date desc limit " + limit + " offset " + offset;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			circulars = con.createQuery(sql).executeAndFetch(Circular.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return circulars;
	}

	private String generateCircularSql(CircularFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from circular";
		} else {
			sql = "select * from circular";
		}

		if (filterParams.getQuery() != null || filterParams.getFromDate() != null || filterParams.getToDate() != null) {
			sql = sql + " where";

			int clauseCount = 0;

			if (filterParams.getFromDate() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}
				sql = sql + " date >='" + filterParams.getFromDate() + "'";
				clauseCount++;
			}

			if (filterParams.getToDate() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}
				sql = sql + " date <='" + filterParams.getToDate() + "'";
				clauseCount++;
			}

			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(subject) like '" + queryString + "')";
			}
		}

		return sql;
	}

	// update
	public boolean updateCircular(List<String> messages, Circular circular) {

		boolean success = false;
		Sql2o sql2o = databaseService.getSql2o();
		String sql = "update circular set date=:date, subject=:subject, fileName=:fileName, fileMime=:fileMime, fileBytes=:fileBytes where id=:id";

		try (Connection con = sql2o.beginTransaction()) {
			con.createQuery(sql).addParameter("date", circular.getDate())
					.addParameter("subject", circular.getSubject()).addParameter("fileName", circular.getFileName())
					.addParameter("fileMime", circular.getFileMime()).addParameter("fileBytes", circular.getFileBytes())
					.addParameter("id", circular.getId()).executeUpdate();

			con.commit();
			success = true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return success;
	}

}