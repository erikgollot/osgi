package com.zenika.formation.osgi.service.consumer.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that displays hit counters.
 * @author François Fornaciari
 */
public class HitCounterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number of visited pages by alias.
	 */
	private Map<String, Integer> counters = new HashMap<String, Integer>();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = "Zenika - Formation OSGi";
		
		response.getWriter().print("<html><head><title>");
		response.getWriter().print(title);
		response.getWriter().print("</title><style type=\"text/css\">" +
				"body{-moz-box-shadow: 5px 5px 18px #000000;-webkit-box-shadow: 5px 5px 18px #000000;box-shadow: 5px 5px 18px #000000;padding: 10px;margin: 20px}" +
				"h1{background-color: #E5E3DF; padding: 5px}" +
				"table{border:1px solid black; border-collapse: collapse; padding: 5px; width: 50%}" +
				"th{border:1px solid black; padding: 5px; background-color: #E5E3DF}" +
				"td{border:1px solid black; padding: 5px; text-align: center}" +
				"</style></head><body>");
		response.getWriter().print("<h1>Number of visited pages</h1>");
		response.getWriter().print("<table>");
		response.getWriter().print("<tr><th>Alias</th><th>Count</th></tr>");
		
		Set<String> aliases = counters.keySet();
		for (String alias : aliases) {
			response.getWriter().print("<tr>");
			response.getWriter().print("<td>");
			response.getWriter().print(alias);
			response.getWriter().print("</td>");
			response.getWriter().print("<td>");
			response.getWriter().print(counters.get(alias));
			response.getWriter().print("</td>");
			response.getWriter().print("</tr>");
		}
		
		response.getWriter().print("</table>");
		response.getWriter().print("</body></html>");
	}
	
}