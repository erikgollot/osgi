package com.zenika.formation.osgi.logservice.provider.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {
	BasicLogService service;
	ServiceRegistration<LogService> sr = null;

	@Override
	public void start(BundleContext context) throws Exception {
		service = new BasicLogService();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("nature", "basic");
		sr = context.registerService(LogService.class, service, props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (sr != null) {
			sr.unregister();
		}

	}

}
