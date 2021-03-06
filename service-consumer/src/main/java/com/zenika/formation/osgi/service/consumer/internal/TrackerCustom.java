package com.zenika.formation.osgi.service.consumer.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class TrackerCustom implements ServiceTrackerCustomizer<LogService, LogService> {
	BundleContext context;
	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	@Override
	public LogService addingService(ServiceReference<LogService> reference) {
		LogService service = getContext().getService(reference);
		service.log(LogService.LOG_DEBUG, "Coucou c'est erik et �a marche, from bundle "
				+ context.getBundle().getBundleId());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference<LogService> reference,
			LogService service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedService(ServiceReference<LogService> reference,
			LogService service) {
		
		service.log(1, "I leave..., from bundle "
				+ context.getBundle().getBundleId());
	}

}
