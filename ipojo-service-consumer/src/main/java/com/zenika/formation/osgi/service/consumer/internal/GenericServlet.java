package com.zenika.formation.osgi.service.consumer.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

/**
 * Generic Servlet implementation.
 * 
 * @author François Fornaciari
 */
@Component(name = "GenericServlet")
@Provides
public class GenericServlet extends HttpServlet {
	@ServiceProperty(value = "/generic")
	String alias;

	@Property
	String text;

//	@Requires
//	EventAdmin eventAdmin;

	@Publishes(name = "publisher", dataKey="context", topics = PAGE_VISITED)
	private Publisher pub;

	private static final long serialVersionUID = 1L;

	public GenericServlet() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		sendPageVisited(request.getServletPath());

		String title = "Zenika - Formation OSGi";
		String top = "Generic Servlet";

		response.getWriter().print("<html><head><title>");
		response.getWriter().print(title);
		response.getWriter()
				.print("</title></head><body style=\"-moz-box-shadow: 5px 5px 18px #000000;-webkit-box-shadow: 5px 5px 18px #000000;box-shadow: 5px 5px 18px #000000;padding: 10px;margin: 20px\">");
		response.getWriter().print(
				"<h1 style=\"background-color: #E5E3DF; padding: 5px\">" + top
						+ "</h1>");
		response.getWriter().print(
				"<div style=\"padding: 5px;\"><b>Text:</b> " + text + "</div>");
		response.getWriter().print("</body></html>");
	}

	public static final String PAGE_VISITED = "PAGE_VISITED";

	private void sendPageVisited(String contextPath) {
		pub.sendData(contextPath);
//		if (eventAdmin != null) {
//			Dictionary<String, String> eventProps = new Hashtable<String, String>();
//			eventProps.put("context", contextPath);
//			eventAdmin.postEvent(new Event(PAGE_VISITED, eventProps));
//		}
	}
}
