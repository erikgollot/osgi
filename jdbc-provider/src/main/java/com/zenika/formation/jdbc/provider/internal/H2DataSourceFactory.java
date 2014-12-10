package com.zenika.formation.jdbc.provider.internal;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.h2.Driver;
import org.h2.jdbcx.JdbcDataSource;
import org.osgi.service.jdbc.DataSourceFactory;

@Component(name="H2DataSourceFactory")
@Provides
public class H2DataSourceFactory implements DataSourceFactory {

	@ServiceProperty(name=DataSourceFactory.OSGI_JDBC_DRIVER_CLASS,value="org.h2.Driver")
	String jdbcDriverClassName;
	
	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		return createH2DataSource(props);
	}

	private JdbcDataSource createH2DataSource(Properties props) {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(props.getProperty(DataSourceFactory.JDBC_URL));
		ds.setUser(props.getProperty(DataSourceFactory.JDBC_USER));
		ds.setPassword(props.getProperty(DataSourceFactory.JDBC_PASSWORD));
		return ds;
	}

	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(
			Properties props) throws SQLException {
		return createH2DataSource(props);
	}

	@Override
	public XADataSource createXADataSource(Properties props)
			throws SQLException {
		return createH2DataSource(props);
	}

	@Override
	public Driver createDriver(Properties props) throws SQLException {
		return new Driver();
	}

}
