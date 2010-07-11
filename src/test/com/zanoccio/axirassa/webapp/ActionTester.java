
package test.com.zanoccio.axirassa.webapp;

import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.util.StrutsTestCaseHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.XWorkTestCase;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.DefaultActionValidatorManager;
import com.opensymphony.xwork2.validator.ValidationException;
import com.zanoccio.axirassa.util.HibernateUtil;

public class ActionTester extends XWorkTestCase {

	protected static Session session;
	protected static SessionFactory sessionFactory;

	public static String test_db = "axir_test";


	@BeforeClass
	public void beforeClass() {
		Configuration configuration = new Configuration();
		configuration.setProperty("connection.url", "jdbc:mysql://localhost/" + test_db);
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.getCurrentSession();
	}


	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		StrutsTestCaseHelper.setUp();

		initDispatcher(null);
	}


	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		StrutsTestCaseHelper.tearDown();
	}


	protected Dispatcher initDispatcher(Map<String, String> parameters) {
		Dispatcher dispatcher = StrutsTestCaseHelper.initDispatcher(new MockServletContext(), parameters);
		configurationManager = dispatcher.getConfigurationManager();
		configuration = configurationManager.getConfiguration();
		container = configuration.getContainer();

		return dispatcher;
	}


	//
	// Action Validation
	//

	public void validateAction(ActionSupport action) throws ValidationException {
		ActionValidatorManager avm = new DefaultActionValidatorManager();
		avm.validate(action, "");
	}


	public void assertFieldErrorExists(String name, ValidationAware action) {
		Map<String, List<String>> errors = action.getFieldErrors();
		assertNotNull(errors);
		assertNotNull(errors.get(name));
		assertTrue(errors.get(name).size() > 0);
	}

}
