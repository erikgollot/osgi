package com.zenika.formation.osgi.service.consumer.internal;

import org.osgi.framework.Bundle;

public class ServletDecl {
	String context;
	String className;
	Bundle bundle;
	
	public ServletDecl(String context, String className, Bundle bundle) {
		super();
		this.context = context;
		this.className = className;
		this.bundle = bundle;
	}
	public String getContext() {
		return context;
	}
	public String getClassName() {
		return className;
	}
	public Bundle getBundle() {
		return bundle;
	}
	
}
