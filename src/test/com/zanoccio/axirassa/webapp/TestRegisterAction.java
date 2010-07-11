
package test.com.zanoccio.axirassa.webapp;

import org.junit.Test;

import com.opensymphony.xwork2.validator.ValidationException;
import com.zanoccio.axirassa.webapp.RegisterAction;

public class TestRegisterAction extends ActionTester {

	@Test
	public void test() {
		assertEquals(2, 2);
	}


	@Test
	public void test2() throws ValidationException {
		assertEquals(3, 3);
		RegisterAction ra = new RegisterAction();
		validateAction(ra);
		assertFieldErrorExists("email", ra);
		assertFieldErrorExists("password", ra);
		assertNotNull("Foo");
	}

}
