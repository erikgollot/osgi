package com.zenika.formation.osgi.logservice.provider.internal;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class BasicLogService  implements LogService{

	@Override
	public void log(int level, String message) {
		System.out.println("log "+level+" : "+message);
	}

	@Override
	public void log(int level, String message, Throwable exception) {
		System.out.println("log "+level+" : "+message);
	}

	@Override
	public void log(ServiceReference sr, int level, String message) {
		System.out.println("log "+level+" : "+message);
		
	}

	@Override
	public void log(ServiceReference sr, int level, String message,
			Throwable exception) {
		System.out.println("log "+level+" : "+message);
	}

}
