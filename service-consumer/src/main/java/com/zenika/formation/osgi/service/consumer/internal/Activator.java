package com.zenika.formation.osgi.service.consumer.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {
	ServiceReference<LogService> sr = null;
	ServiceTracker<LogService, LogService> tracker = null;

	@Override
	public void start(BundleContext context) throws Exception {
		// sr = context.getServiceReference(LogService.class);
		// if (sr != null) {
		// LogService log = context.getService(sr);
		// log.log(1, "Coucou c'est erik et �a marche, from bundle "
		// + context.getBundle().getBundleId());
		// }

		TrackerCustom custom = new TrackerCustom();
		custom.setContext(context);
		
		tracker = new ServiceTracker<LogService, LogService>(
				context,
				context.createFilter("(&(objectClass=org.osgi.service.log.LogService)(nature=basic))"),
				(ServiceTrackerCustomizer<LogService, LogService>) custom);

		tracker.open();
//		LogService ls = tracker.getService();
//		ls.log(1, "Coucou c'est erik et �a marche, from bundle "
//				+ context.getBundle().getBundleId());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (sr != null) {
			context.ungetService(sr);
		}
		if (tracker!=null) {
			tracker.close();
		}

	}

}
