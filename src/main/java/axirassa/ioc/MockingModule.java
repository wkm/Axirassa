
package axirassa.ioc;

import static org.mockito.Mockito.mock;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.hibernate.Session;

/**
 * Provides mock implementations of common services
 * 
 * @author wiktor
 */
@SubModule({ ExternalServicesMockingModule.class, MockPageRenderLinkSourceModule.class })
public class MockingModule {
	public static void contributeServiceOverride(MappedConfiguration<Class<?>, Object> configuration) {
		configuration.add(HibernateSessionSource.class, mock(HibernateSessionSource.class));
		configuration.add(HibernateSessionManager.class, mock(HibernateSessionManager.class));
		configuration.add(Session.class, mock(Session.class));
	}
}
