package in.co.itlabs.business.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Properties;

import org.sql2o.Sql2o;
import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.Quirks;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseService {
	private Sql2o sql2o;

	public DatabaseService() throws SQLException, IOException {

		Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/config.properties"));

//		String mysqlUrl = "jdbc:mysql://localhost:3306/covidapp";
//		String mysqlUser = "user";
//		String mysqlPassword = "password";

		String mysqlUrl = props.getProperty("mysql.url");
		String mysqlUser = props.getProperty("mysql.user");
		String mysqlPassword = props.getProperty("mysql.password");

//		sql2o = new Sql2o(url, user, password);
		final MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setUrl(mysqlUrl);
		dataSource.setUser(mysqlUser);
		dataSource.setPassword(mysqlPassword);

		final Quirks quirks = new NoQuirks() {
			{
				converters.put(LocalDate.class, new LocalDateConverter());
			}
		};

		sql2o = new Sql2o(dataSource, quirks);
	}

	public Sql2o getSql2o() {
		return sql2o;
	}

	private class LocalDateConverter implements Converter<LocalDate> {
		@Override
		public LocalDate convert(final Object val) throws ConverterException {
			if (val instanceof java.sql.Date) {
				return ((java.sql.Date) val).toLocalDate();
			} else {
				return null;
			}
		}

		@Override
		public Object toDatabaseParam(final LocalDate val) {
			if (val == null) {
				return null;
			} else {
				return new java.sql.Date(val.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
			}
		}
	}
}
