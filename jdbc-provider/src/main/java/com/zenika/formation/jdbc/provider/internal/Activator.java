package com.zenika.formation.jdbc.provider.internal;

import org.h2.tools.Server;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.jdbc.DataSourceFactory;

public class Activator implements BundleActivator {
	Server s;
	public void start(BundleContext bundleContext) throws Exception {
		 s = Server.createWebServer();
		 s.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		s.stop();
	}

}
