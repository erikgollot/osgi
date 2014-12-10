package com.zenika.formation.osgi.service.consumer.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generic Servlet implementation.
 * @author François Fornaciari
 */
public class GenericServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String text;
	
	public GenericServlet(String text) {
		this.text = text;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = "Zenika - Formation OSGi";
		String top = "Generic Servlet";
		
		response.getWriter().print("<html><head><title>");
		response.getWriter().print(title);
		response.getWriter().print("</title></head><body style=\"-moz-box-shadow: 5px 5px 18px #000000;-webkit-box-shadow: 5px 5px 18px #000000;box-shadow: 5px 5px 18px #000000;padding: 10px;margin: 20px\">");
		response.getWriter().print("<h1 style=\"background-color: #E5E3DF; padding: 5px\">" + top + "</h1>");
		response.getWriter().print("<div style=\"padding: 5px;\"><b>Text:</b> " + text + "</div>");
		response.getWriter().print("</body></html>");
	}
}
