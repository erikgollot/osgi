package com.zenika.formation.osgi.service.consumer.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {
	ServiceReference<LogService> sr = null;
	ServiceTracker<LogService, LogService> tracker = null;
	ServiceReference<HttpService> httpServiceRef = null;
	HttpService http = null;
	Hashtable<Long, List<String>> servletAlias = new Hashtable<Long, List<String>>();
	List<ServletDecl> beforeHttp = new ArrayList<ServletDecl>();

	@Override
	public void start(BundleContext context) throws Exception {
		// sr = context.getServiceReference(LogService.class);
		// if (sr != null) {
		// LogService log = context.getService(sr);
		// log.log(1, "Coucou c'est erik et �a marche, from bundle "
		// + context.getBundle().getBundleId());
		// }

		startServiceTracker(context);
		// LogService ls = tracker.getService();
		// ls.log(1, "Coucou c'est erik et �a marche, from bundle "
		// + context.getBundle().getBundleId());

		startBundleTracker(context);
	}

	private void startBundleTracker(BundleContext context) {
		BundleTracker<String> bTrack = new BundleTracker<String>(context,
				Bundle.ACTIVE, null) {

			@Override
			public String addingBundle(Bundle bundle, BundleEvent event) {
				if (http == null) {
					loadHttpService();
					if (!beforeHttp.isEmpty()) {
						registerServlets(beforeHttp);
					}
				}
				List<ServletDecl> configs = null;
				if ((configs = findServletDecl(bundle)) != null) {
					if (http!=null) {
						registerServlets(configs);
					}
					else {
						beforeHttp.addAll(configs);
					}
				}
				return bundle.toString();
			}

			private void registerServlets(List<ServletDecl> configs) {
				for (ServletDecl s : configs) {
					try {
						http.registerServlet(s.getContext(),
								(Servlet) s.getBundle().loadClass(s.getClassName())
										.newInstance(), null, null);
						addAlias(s.getBundle().getBundleId(), s.getContext());
					} catch (InstantiationException | IllegalAccessException
							| ClassNotFoundException | ServletException
							| NamespaceException e) {
						e.printStackTrace();
					}
				}
			}

			private List<ServletDecl> findServletDecl(Bundle bundle) {
				Dictionary<String, String> headers = bundle.getHeaders();
				List<ServletDecl> servlets = null;
				Enumeration it = headers.keys();
				while (it.hasMoreElements()) {
					String key = (String) it.nextElement();
					if ("Servlet-Map".equals(key)) {
						servlets = new ArrayList<ServletDecl>();
						String servletDecls[] = headers.get(key).split(",");

						for (int i = 0; i < servletDecls.length; i++) {
							String servletDecl = servletDecls[i];
							String stk2[] = servletDecl.split("=");
							String urlFragment = stk2[0];
							String servletName = stk2[1];
							servlets.add(new ServletDecl(urlFragment,
									servletName, bundle));
						}
					}
				}
				return servlets;
			}

			private void loadHttpService() {
				httpServiceRef = context.getServiceReference(HttpService.class);
				if (httpServiceRef != null) {
					http = context.getService(httpServiceRef);
				}
			}

			@Override
			public void remove(Bundle bundle) {
				List<String> urls = servletAlias.get(bundle.getBundleId());
				if (urls != null) {
					for (String url : urls) {
						http.unregister(url);
					}
					servletAlias.remove(bundle.getBundleId());
				}
			}

		};

		bTrack.open();
	}

	private void startServiceTracker(BundleContext context)
			throws InvalidSyntaxException {
		TrackerCustom custom = new TrackerCustom();
		custom.setContext(context);

		tracker = new ServiceTracker<LogService, LogService>(
				context,
				context.createFilter("(&(objectClass=org.osgi.service.log.LogService)(nature=basic))"),
				(ServiceTrackerCustomizer<LogService, LogService>) custom);

		tracker.open();
	}

	protected void addAlias(long bundleId, String urlFragment) {
		List<String> urls = servletAlias.get(bundleId);
		if (urls == null) {
			urls = new ArrayList<String>();
			servletAlias.put(bundleId, urls);
		}
		urls.add(urlFragment);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (sr != null) {
			context.ungetService(sr);
		}
		if (tracker != null) {
			tracker.close();
		}

	}

}
