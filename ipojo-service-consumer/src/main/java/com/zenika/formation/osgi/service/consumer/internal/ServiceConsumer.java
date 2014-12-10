package com.zenika.formation.osgi.service.consumer.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;

@Component(name = "ServiceConsumer")
@Provides
public class ServiceConsumer implements ManagedServiceFactory {

	public ServiceConsumer(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("service.pid", "com.zenika.formation.servlet.configuration");
		bundleContext.registerService(ManagedServiceFactory.class.getName(),
				this, props);
	}
/**
 * EventAdmin for async publish
 */
	@Requires
	EventAdmin eventAdmin;
	
	/**
	 * OSGi BundleContext.
	 */
	private BundleContext bundleContext = null;

	/**
	 * OSGi LogService.
	 */
	private LogService logService;

	/**
	 * OSGi HttpService.
	 */

	private HttpService httpService;

	/**
	 * OSGi BundleTracker.
	 */
	private BundleTracker<List<String>> bundleTracker;

	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void startTracker() throws Exception {

		bundleTracker = new BundleTracker<List<String>>(bundleContext,
				Bundle.ACTIVE, null) {
			public List<String> addingBundle(Bundle bundle, BundleEvent event) {
				// Registering servlets for this bundle if any
				return registerServlets(bundle);
			}

			public void removedBundle(Bundle bundle, BundleEvent event,
					List<String> object) {
				// Unregistering tracked aliases
				unregisterAliases(object);
			}
		};
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		bundleTracker.close();
		bundleTracker = null;
		// Removing OSGi listener
		// bundleContext.removeServiceListener(this);
	}

	/**
	 * Method called when the LogService is registered.
	 * 
	 * @param logService
	 *            LogService instance
	 */
	@Bind(filter = "(nature=basic)",optional=true)
	public void bindLogService(LogService logService) {
		this.logService = logService;
		log(LogService.LOG_INFO, "Message from bundle ["
				+ bundleContext.getBundle().getBundleId() + "]", null);
	}

	/**
	 * Method called before the LogService is unregistered.
	 * 
	 * @param logService
	 *            LogService instance
	 */
	@Unbind
	public void unbindLogService(LogService logService) {
		this.logService = null;
	}

	/**
	 * Method called when the HttpService is registered.
	 * 
	 * @param httpService
	 *            HttpService instance
	 */
	@Bind
	public void bindHttpService(HttpService httpService,
			ServiceReference<HttpService> reference) {
		this.httpService = httpService;
	}
	
	@Validate
	public void start() {
		try {

			// Register servlet identified in update but not already registered
			if (!toRegister.isEmpty()) {
				for (String alias : toRegister.keySet()) {
					registerGenericServlet(bundleContext.getBundle(), alias,
							toRegister.get(alias).getServlet());
					dynamicServletRegistry.put(toRegister.get(alias).getPid(),
							alias);
				}
				toRegister.clear();
			}
			
			startTracker();
			bundleTracker.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Starts tracking servlets

	}

	/**
	 * Method called before the HttpService is unregistered.
	 * 
	 * @param httpService
	 *            HttpService instance
	 */
	@Unbind
	public void unbindHttpService(HttpService httpService,
			ServiceReference<HttpService> reference) {
		// Stops tracking servlets
		try {
			stop(reference.getBundle().getBundleContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.httpService = null;
	}

	/**
	 * Logs a message using the LogService.
	 * 
	 * @param level
	 *            The log level
	 * @param message
	 *            The log message
	 * @param exception
	 *            The log exception
	 */
	public void log(int level, String message, Exception exception) {
		if (this.logService == null) {
			System.out.println("LogService not registered");
		} else {
			this.logService.log(level, message, exception);
		}
	}

	/**
	 * Registers servlets if any.
	 * 
	 * @param bundle
	 *            Bundle to analyze
	 */
	private List<String> registerServlets(Bundle bundle) {
		List<String> aliases = new LinkedList<String>();
		Map<String, String> servletMap = getServletHeader(bundle);
		for (String alias : servletMap.keySet()) {
			String className = servletMap.get(alias);
			try {
				registerServlet(bundle, alias, className);
				aliases.add(alias);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return aliases;
	}

	/**
	 * Registers a servlet into the URI namespace.
	 * 
	 * @param bundle
	 *            Bundle used to load the servlet class
	 * @param alias
	 *            Name in the URI namespace at which the servlet is registered
	 * @param className
	 *            Servlet class name.
	 * @throws Exception
	 *             If the registration fails
	 */
	private void registerServlet(Bundle bundle, String alias, String className)
			throws Exception {
		Class<?> clazz = bundle.loadClass(className);
		if (clazz != null) {
			// Servlet creation and registration
			Servlet servlet = (Servlet) clazz.newInstance();
			log(LogService.LOG_INFO,
					"Registering servlet with alias: " + alias, null);
			httpService.registerServlet(alias, servlet, null, null);
		}
	}

	/**
	 * Unregisters aliases.
	 * 
	 * @param bundle
	 *            List of aliases to unregister
	 */
	private void unregisterAliases(List<String> aliases) {
		for (String alias : aliases) {
			unregisterAlias(alias);
		}
	}

	/**
	 * Unregisters alias.
	 * 
	 * @param bundle
	 *            Alias to unregister
	 */
	private void unregisterAlias(String alias) {
		log(LogService.LOG_INFO, "Unregistering servlet with alias " + alias,
				null);
		httpService.unregister(alias);
	}

	/**
	 * Gets the couple (key,value) of the Servlet-Map MANIFEST header
	 * 
	 * @param bundle
	 *            Bundle to analyze
	 * @return The couple (key,value) of the Servlet-Map MANIFEST header
	 */
	private Map<String, String> getServletHeader(Bundle bundle) {
		Map<String, String> servletMap = new HashMap<String, String>();
		String servletHeader = (String) bundle.getHeaders().get("Servlet-Map");
		if (servletHeader != null) {
			String clauses[] = servletHeader.split(",");
			for (String clause : clauses) {
				String parts[] = clause.trim().split("=");
				if (parts.length == 2) {
					servletMap.put(parts[0], parts[1]);
				}
			}
		}
		return servletMap;
	}

	@Override
	public String getName() {
		return "Me";
	}

	private void registerGenericServlet(Bundle bundle, String alias,
			HttpServlet servlet) throws Exception {
		log(LogService.LOG_INFO, "Registering servlet with alias: " + alias,
				null);
		httpService.registerServlet(alias, servlet, null, null);

	}

	Hashtable<String, String> dynamicServletRegistry = new Hashtable<String, String>();
	Hashtable<String, ToRegisterServlet> toRegister = new Hashtable<String, ToRegisterServlet>();

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
			throws ConfigurationException {
		System.out.println("update call : " + pid);
		// If already known, remove first
		if (dynamicServletRegistry.get(pid) != null) {
			unregisterAlias(dynamicServletRegistry.get(pid));
		}
		try {
			if (httpService != null) {
				registerGenericServlet(bundleContext.getBundle(),
						(String) properties.get("alias"), new GenericServlet(
								(String) properties.get("text"),eventAdmin));
				dynamicServletRegistry.put(pid,
						(String) properties.get("alias"));
			} else {
				toRegister.put((String) properties.get("alias"),
						new ToRegisterServlet(pid, new GenericServlet(
								(String) properties.get("text"),eventAdmin)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleted(String pid) {
		if (dynamicServletRegistry.get(pid) != null) {
			unregisterAlias(dynamicServletRegistry.get(pid));
			dynamicServletRegistry.remove(pid);
		}
	}
}
