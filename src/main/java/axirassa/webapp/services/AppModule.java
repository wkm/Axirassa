
package axirassa.webapp.services;

import java.io.IOException;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;
import org.tynamo.security.SecuritySymbols;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {
	@Inject
	private Session session;


	public static void bind(ServiceBinder binder) {
		// binder.bind(MyServiceInterface.class, MyServiceImpl.class);

		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(AuthorizingRealm.class, EntityRealm.class);
	}


	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		// As you add localised message catalogs and other assets, you can
		// extend this list of locales (it's a comma separated series of locale
		// names;

		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");

		// The factory default is true but during the early stages of an
		// application overriding to false is a good idea. In addition, this is
		// often overridden on the command line as
		// -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

		// tapestry-security configuration
		configuration.add(SecuritySymbols.LOGIN_URL, "/user/login");
		configuration.add(SecuritySymbols.UNAUTHORIZED_URL, "/index");
	}


	/**
	 * This is a service definition, the service will be named "TimingFilter".
	 * The interface, RequestFilter, is used within the RequestHandler service
	 * pipeline, which is built from the RequestHandler service configuration.
	 * Tapestry IoC is responsible for passing in an appropriate Logger
	 * instance. Requests for static resources are handled at a higher level, so
	 * this filter will only be invoked for Tapestry related requests.
	 * 
	 * <p>
	 * Service builder methods are useful when the implementation is inline as
	 * an inner class (as here) or require some other kind of special
	 * initialization. In most cases, use the static bind() method instead.
	 * 
	 * <p>
	 * If this method was named "build", then the service id would be taken from
	 * the service interface and would be "RequestFilter". Since Tapestry
	 * already defines a service named "RequestFilter" we use an explicit
	 * service id that we can reference inside the contribution method.
	 */
	public RequestFilter buildTimingFilter(final Logger log) {
		return new RequestFilter() {
			@Override
			public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
				long startTime = System.currentTimeMillis();

				try {
					// The responsibility of a filter is to invoke the
					// corresponding method
					// in the handler. When you chain multiple filters together,
					// each filter
					// received a handler that is a bridge to the next filter.

					return handler.service(request, response);
				} finally {
					long elapsed = System.currentTimeMillis() - startTime;

					// log.info(String.format("Request time: %d ms", elapsed));
				}
			}
		};

	}


	@Scope(ScopeConstants.PERTHREAD)
	public static MessagingSessionManager buildMessagingSessionManager(PerthreadManager perthreadManager)
	        throws HornetQException {
		MessagingSessionManagerImpl sessionManager = new MessagingSessionManagerImpl();
		perthreadManager.addThreadCleanupListener(sessionManager);
		return sessionManager;
	}


	public static MessagingSession buildMessagingSession(MessagingSessionManager sessionManager,
	        PropertyShadowBuilder propertyShadowBuilder) throws HornetQException {
		return propertyShadowBuilder.build(sessionManager, "session", MessagingSession.class);
	}


	@Scope(ScopeConstants.PERTHREAD)
	public EmailNotifyService buildEmailNotifyService(MessagingSession messagingSession) throws HornetQException {
		return new EmailNotifyServiceImpl(messagingSession);
	}


	/**
	 * This is a contribution to the RequestHandler service configuration. This
	 * is how we extend Tapestry using the timing filter. A common use for this
	 * kind of filter is transaction management or security. The @Local
	 * annotation selects the desired service by type, but only from the same
	 * module. Without @Local, there would be an error due to the other
	 * service(s) that implement RequestFilter (defined in other modules).
	 */
	public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration, @Local RequestFilter filter) {
		// Each contribution to an ordered configuration has a name, When
		// necessary, you may
		// set constraints to precisely control the invocation order of the
		// contributed filter
		// within the pipeline.

		configuration.add("Timing", filter);
	}


	public void contributeWebSecurityManager(Configuration<Realm> configuration) {
		EntityRealm realm = new EntityRealm(session);
		realm.setCredentialsMatcher(new UserCredentialsMatcher(session));
		configuration.add(realm);
	}


	public void contributeIgnoredPathsFilter(Configuration<String> configuration) {
		configuration.add("/push/.*");
		configuration.add("/stream/.*");
	}
}
