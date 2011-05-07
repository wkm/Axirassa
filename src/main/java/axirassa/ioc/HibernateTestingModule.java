
package axirassa.ioc;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.hibernate.cfg.Configuration;

import axirassa.util.HibernateTools;

@SubModule(HibernateCoreModule.class)
public class HibernateTestingModule {
	public static HibernateConfigurer decorateDefaultHibernateConfigurer(HibernateConfigurer configuration) {
		return new HibernateTestConfigurer();
	}
}

class HibernateTestConfigurer implements HibernateConfigurer {
	@Override
	public void configure(Configuration configuration) {
		configuration.configure();
		HibernateTools.configureTesting(configuration);
	}
}
