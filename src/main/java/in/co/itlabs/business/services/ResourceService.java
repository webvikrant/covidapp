package in.co.itlabs.business.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import in.co.itlabs.Application;
import in.co.itlabs.business.entities.City;
import in.co.itlabs.business.entities.Resource;

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
				+ " remark, verified, createdBy, createdAt, updatedBy, updatedAt)"
				+ " values(:cityId, :type, :name, :address, :phone1, :phone2, :phone3,"
				+ " :remark, :verified, :createdBy, :createdAt, :updatedBy, :updatedAt)";

		try (Connection con = sql2o.beginTransaction()) {
			int resourceId = con.createQuery(insertSql).addParameter("cityId", resource.getCityId())
					.addParameter("type", resource.getType()).addParameter("name", resource.getName())
					.addParameter("address", resource.getAddress()).addParameter("phone1", resource.getPhone1())
					.addParameter("phone2", resource.getPhone2()).addParameter("phone3", resource.getPhone3())
					.addParameter("remark", resource.getRemark()).addParameter("verified", resource.isVerified())
					.addParameter("createdBy", resource.getCreatedBy())
					.addParameter("createdAt", resource.getCreatedAt())
					.addParameter("updatedBy", resource.getUpdatedBy())
					.addParameter("updatedAt", resource.getUpdatedAt()).executeUpdate().getKey(Integer.class);

			con.commit();
			newResourceId = resourceId;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			messages.add(e.getMessage());
		}
		return newResourceId;
	}

	// read many
	public List<Resource> getResources() {
		List<Resource> resources = null;

		Sql2o sql2o = databaseService.getSql2o();

		try (Connection con = sql2o.open()) {
			String sql = "select * from resource";
			resources = con.createQuery(sql).executeAndFetch(Resource.class);
			con.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return resources;
	}

}