package com.zenika.formation.jdbc.consumer.internal;

import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	public void start(final BundleContext bundleContext) throws Exception {
		Filter filter = bundleContext.createFilter("(&(objectClass="
				+ DataSourceFactory.class.getName() + ")("
				+ DataSourceFactory.OSGI_JDBC_DRIVER_CLASS + "=org.h2.Driver))");
		ServiceTracker tracker = new ServiceTracker(bundleContext,filter,null);
		tracker.open();
		DataSourceFactory factory = (DataSourceFactory)tracker.getService();
		
		Driver d = factory.createDriver(null);
		System.out.println(d.getMajorVersion()+"."+d.getMinorVersion());
		
		Properties props = new Properties();
		props.put(DataSourceFactory.JDBC_URL, "jdbc:h2:~/test");
		props.put(DataSourceFactory.JDBC_USER, "sa");
		props.put(DataSourceFactory.JDBC_PASSWORD, "");
		DataSource ds = factory.createDataSource(props);
		
		Statement stmt= ds.getConnection().createStatement();
		ResultSet resultSet = stmt.executeQuery("select CURRENT_DATE()");
	    
	    while (resultSet.next()) {
	    	System.out.println("Current date : " + resultSet.getDate(1));
	    }
		
	}

	public void stop(final BundleContext bundleContext) throws Exception {

	}

}
