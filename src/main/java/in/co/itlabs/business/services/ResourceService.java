package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import in.co.itlabs.Application;
import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.PlasmaDonor;
import in.co.itlabs.business.entities.Resource;
import in.co.itlabs.business.entities.User;
import in.co.itlabs.util.PlasmaDonorFilterParams;
import in.co.itlabs.util.ResourceFilterParams;

public class ResourceService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

	private DatabaseService databaseService;

	public ResourceService() {
		databaseService = Application.getDatabaseService();
	}

	// =================================================================================
	// cities
	// =================================================================================

	// read many
	public List<City> getAllCities() {
		List<City> cities = null;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from city";
			cities = con.createQuery(sql).executeAndFetch(City.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return cities;
	}

	public City getCityById(int id) {
		City city = null;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from city where id = :id";
			city = con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(City.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return city;
	}

	// =================================================================================
	// resources
	// =================================================================================

	// create
	public int createResource(List<String> messages, Resource resource) {

		int newResourceId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String insertSql = "insert into resource (cityId, type, name, address, phone1, phone2, phone3,"
				+ " remark, status, createdBy, createdAt, updatedBy, updatedAt, guestName, guestPhone)"
				+ " values(:cityId, :type, :name, :address, :phone1, :phone2, :phone3,"
				+ " :remark, :status, :createdBy, :createdAt, :updatedBy, :updatedAt, :guestName, :guestPhone)";

		try (Connection con = sql2o.beginTransaction()) {
			int resourceId = con.createQuery(insertSql).addParameter("cityId", resource.getCityId())
					.addParameter("type", resource.getType()).addParameter("name", resource.getName())
					.addParameter("address", resource.getAddress()).addParameter("phone1", resource.getPhone1())
					.addParameter("phone2", resource.getPhone2()).addParameter("phone3", resource.getPhone3())
					.addParameter("remark", resource.getRemark()).addParameter("status", resource.getStatus())
					.addParameter("createdBy", resource.getCreatedBy())
					.addParameter("createdAt", resource.getCreatedAt())
					.addParameter("updatedBy", resource.getUpdatedBy())
					.addParameter("updatedAt", resource.getUpdatedAt())
					.addParameter("guestName", resource.getGuestName())
					.addParameter("guestPhone", resource.getGuestPhone()).executeUpdate().getKey(Integer.class);

			con.commit();
			newResourceId = resourceId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newResourceId;
	}

	// read many

	public int getResourcesCount(ResourceFilterParams filterParams) {
		int count = 0;

		String sql = generateResourceSql(filterParams, true);
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<Resource> getResources(int offset, int limit, ResourceFilterParams filterParams) {
		List<Resource> resources = null;

		String sql = generateResourceSql(filterParams, false);
		sql = sql + " order by updatedAt desc limit " + limit + " offset " + offset;

		String citySql = "select * from city where id=:id";
		String userSql = "select * from user where id=:id";

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			resources = con.createQuery(sql).executeAndFetch(Resource.class);
			con.close();

			for (Resource resource : resources) {
				City city = con.createQuery(citySql).addParameter("id", resource.getCityId())
						.executeAndFetchFirst(City.class);

				User user = con.createQuery(userSql).addParameter("id", resource.getUpdatedBy())
						.executeAndFetchFirst(User.class);

				resource.setCity(city);
				resource.setUpdatedByUser(user);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return resources;
	}

	private String generateResourceSql(ResourceFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from resource";
		} else {
			sql = "select * from resource";
		}

		if (filterParams.getCity() != null || filterParams.getType() != null || filterParams.getStatus() != null
				|| filterParams.getQuery() != null) {
			sql = sql + " where";

			int clauseCount = 0;
			if (filterParams.getCity() != null) {
				sql = sql + " cityId=" + filterParams.getCity().getId();
				clauseCount++;
			}

			if (filterParams.getType() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}
				sql = sql + " type='" + filterParams.getType() + "'";
				clauseCount++;
			}

			if (filterParams.getStatus() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}
				sql = sql + " status='" + filterParams.getStatus() + "'";
				clauseCount++;
			}

			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(name) like '" + queryString + "' or lower(address) like '" + queryString + "')";
			}

		}

		return sql;
	}

	public int getResourcesCountForGuests(ResourceFilterParams filterParams) {
		int count = 0;

		String sql = generateResourceSqlForGuests(filterParams, true);
		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<Resource> getResourcesForGuests(int offset, int limit, ResourceFilterParams filterParams) {
		List<Resource> resources = null;

		String sql = generateResourceSqlForGuests(filterParams, false);
		sql = sql + " order by updatedAt desc limit " + limit + " offset " + offset;

		String citySql = "select * from city where id=:id";
		String userSql = "select * from user where id=:id";

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			resources = con.createQuery(sql).executeAndFetch(Resource.class);
			con.close();

			for (Resource resource : resources) {
				City city = con.createQuery(citySql).addParameter("id", resource.getCityId())
						.executeAndFetchFirst(City.class);

				User user = con.createQuery(userSql).addParameter("id", resource.getUpdatedBy())
						.executeAndFetchFirst(User.class);

				resource.setCity(city);
				resource.setUpdatedByUser(user);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return resources;
	}

	private String generateResourceSqlForGuests(ResourceFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from resource";
		} else {
			sql = "select * from resource";
		}

		if (filterParams.getCity() != null || filterParams.getType() != null || filterParams.getStatus() != null
				|| filterParams.getQuery() != null) {
			sql = sql + " where";

			int clauseCount = 0;
			if (filterParams.getCity() != null) {
				sql = sql + " cityId=" + filterParams.getCity().getId();
				clauseCount++;
			}

			if (filterParams.getType() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}
				sql = sql + " type='" + filterParams.getType() + "'";
				clauseCount++;
			}

			// status - verified or pending, created by guest or user
			if (clauseCount > 0) {
				sql = sql + " and";
			}
			sql = sql + " ( (createdBy=0 and status='Verified')"
					+ " or  (createdBy>0 and (status='Pending' or status='Verified')) )";
			clauseCount++;

			// user
			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(name) like '" + queryString + "' or lower(address) like '" + queryString + "')";
			}

		}

		return sql;
	}

	// update
	public boolean updateResource(List<String> messages, Resource resource) {

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

	// =================================================================================
	// plasma donors
	// =================================================================================

	// create
	public int createPlasmaDonor(List<String> messages, PlasmaDonor plasmaDonor) {

		int newPlasmaDonorId = 0;
		Sql2o sql2o = databaseService.getSql2o();
		String insertSql = "insert into plasma_donor (bloodGroup, gender, name, age, phone, pincode, address,"
				+ " infectionDate , recoveryDate, available, verified, remark, createdAt, updatedAt)"
				+ " values(:bloodGroup, :gender, :name, :age, :phone, :pincode, :address,"
				+ " :infectionDate, :recoveryDate, :available, :verified, :remark, :createdAt, :updatedAt)";

		try (Connection con = sql2o.beginTransaction()) {
			int resourceId = con.createQuery(insertSql).addParameter("bloodGroup", plasmaDonor.getBloodGroup())
					.addParameter("gender", plasmaDonor.getGender()).addParameter("name", plasmaDonor.getName())
					.addParameter("age", plasmaDonor.getAge()).addParameter("phone", plasmaDonor.getPhone())
					.addParameter("pincode", plasmaDonor.getPincode()).addParameter("address", plasmaDonor.getAddress())
					.addParameter("infectionDate", plasmaDonor.getInfectionDate())
					.addParameter("recoveryDate", plasmaDonor.getRecoveryDate())
					.addParameter("available", plasmaDonor.isAvailable())
					.addParameter("verified", plasmaDonor.isVerified()).addParameter("remark", plasmaDonor.getRemark())
					.addParameter("createdAt", plasmaDonor.getCreatedAt())
					.addParameter("updatedAt", plasmaDonor.getUpdatedAt()).executeUpdate().getKey(Integer.class);

			con.commit();
			newPlasmaDonorId = resourceId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newPlasmaDonorId;
	}

	// read many

	public int getPlasmaDonorsCount(PlasmaDonorFilterParams filterParams) {
		int count = 0;

		String sql = generatePlasmaDonorSql(filterParams, true);

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			count = con.createQuery(sql).executeScalar(Integer.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return count;
	}

	public List<PlasmaDonor> getPlasmaDonors(int offset, int limit, PlasmaDonorFilterParams filterParams) {
		List<PlasmaDonor> plasmaDonors = null;

		String sql = generatePlasmaDonorSql(filterParams, false);

		sql = sql + " order by updatedAt desc limit " + limit + " offset " + offset;
		String citySql = "select * from city where id=:id";

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			plasmaDonors = con.createQuery(sql).executeAndFetch(PlasmaDonor.class);

			for (PlasmaDonor plasmaDonor : plasmaDonors) {
				City city = con.createQuery(citySql).addParameter("id", plasmaDonor.getCityId())
						.executeAndFetchFirst(City.class);

				plasmaDonor.setCity(city);
			}

			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return plasmaDonors;
	}

	private String generatePlasmaDonorSql(PlasmaDonorFilterParams filterParams, boolean countSql) {
		String sql = "";
		if (countSql) {
			sql = "select count(id) from plasma_donor";
		} else {
			sql = "select * from plasma_donor";
		}

		if (filterParams.getBloodGroup() != null || filterParams.getGender() != null || filterParams.getCity() != null
				|| filterParams.getVerified() != null || filterParams.getAvailable() != null
				|| filterParams.getQuery() != null) {
			sql = sql + " where";

			int clauseCount = 0;

			if (filterParams.getBloodGroup() != null) {
				sql = sql + " bloodGroup='" + filterParams.getBloodGroup().name() + "'";
				clauseCount++;
			}

			if (filterParams.getGender() != null) {
				sql = sql + " gender='" + filterParams.getGender().name() + "'";
				clauseCount++;
			}

//			if (filterParams.getVerified() != null) {
//				sql = sql + " verified=" + filterParams.getGender();
//				clauseCount++;
//			}
//
//			if (filterParams.getAvailable() != null) {
//				sql = sql + " available=" + filterParams.getAvailable();
//				clauseCount++;
//			}

			if (filterParams.getCity() != null) {
				sql = sql + " cityId=" + filterParams.getCity().getId();
				clauseCount++;
			}

			if (filterParams.getQuery() != null) {
				if (clauseCount > 0) {
					sql = sql + " and";
				}

				String queryString = "%" + filterParams.getQuery().trim().toLowerCase() + "%";
				sql = sql + " (lower(name) like '" + queryString + "' or lower(address) like '" + queryString + "')";
			}

		}

		return sql;
	}

	// update
	public boolean updatePlasmaDonor(List<String> messages, PlasmaDonor plasmaDonor) {

		boolean success = false;
//		Sql2o sql2o = databaseService.getSql2o();
//		String insertSql = "update resource set cityId=:cityId, type=:type, name=:name, address=:address,"
//				+ " phone1=:phone1, phone2=:phone2, phone3=:phone3, remark=:remark, verified=:verified,"
//				+ " createdBy=:createdBy, createdAt=:createdAt, updatedBy=:updatedBy, updatedAt=:updatedAt"
//				+ " where id=:id";

//		try (Connection con = sql2o.beginTransaction()) {
//			con.createQuery(insertSql).addParameter("cityId", plasmaDonor.getCityId())
//					.addParameter("type", plasmaDonor.getType()).addParameter("name", plasmaDonor.getName())
//					.addParameter("address", plasmaDonor.getAddress()).addParameter("phone1", plasmaDonor.getPhone1())
//					.addParameter("phone2", plasmaDonor.getPhone2()).addParameter("phone3", plasmaDonor.getPhone3())
//					.addParameter("remark", plasmaDonor.getRemark()).addParameter("verified", plasmaDonor.isVerified())
//					.addParameter("createdBy", plasmaDonor.getCreatedBy())
//					.addParameter("createdAt", plasmaDonor.getCreatedAt())
//					.addParameter("updatedBy", plasmaDonor.getUpdatedBy())
//					.addParameter("updatedAt", plasmaDonor.getUpdatedAt()).addParameter("id", plasmaDonor.getId())
//					.executeUpdate();

//			con.commit();
//			success = true;
//		} catch (Exception e) {
//			logger.debug(e.getMessage());
//			messages.add(e.getMessage());
//		}
		return success;
	}
}