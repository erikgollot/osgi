package com.zenika.formation.osgi.logservice.provider.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

    private ServiceRegistration<LogService> serviceRegistration;

    public void start(BundleContext bundleContext) {
    	LogService logService = new BasicLogService();
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put("nature", "basic");
        serviceRegistration = bundleContext.registerService(LogService.class, logService, properties);
    }

    public void stop(BundleContext context) {
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
    }
}
