package com.zenika.formation.osgi.service.consumer.internal;

public class ToRegisterServlet {
	String pid;
	GenericServlet servlet;
	public String getPid() {
		return pid;
	}
	public GenericServlet getServlet() {
		return servlet;
	}
	public ToRegisterServlet(String pid, GenericServlet servlet) {
		super();
		this.pid = pid;
		this.servlet = servlet;
	}
	
}
