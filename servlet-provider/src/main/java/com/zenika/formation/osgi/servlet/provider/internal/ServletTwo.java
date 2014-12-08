package com.zenika.formation.osgi.servlet.provider.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple Servlet Two.
 * @author François Fornaciari
 */
public class ServletTwo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = "Zenika - Formation OSGi";
		String top = "Simple Servlet";
		String alias = "/two";
		String clazz = "ServletTwo";
		
		response.getWriter().print("<html><head><title>");
		response.getWriter().print(title);
		response.getWriter().print("</title></head><body style=\"-moz-box-shadow: 5px 5px 18px #000000;-webkit-box-shadow: 5px 5px 18px #000000;box-shadow: 5px 5px 18px #000000;padding: 10px;margin: 20px\">");
		response.getWriter().print("<h1 style=\"background-color: #E5E3DF; padding: 5px\">" + top + "</h1>");
		response.getWriter().print("<div style=\"padding: 5px;\"><b>Alias:</b> " + alias + "</div><div style=\"padding: 5px;\"><b>Class:</b> " + clazz + "</div>");
		response.getWriter().print("</body></html>");
	}

}
