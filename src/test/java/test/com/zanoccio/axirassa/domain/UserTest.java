
package test.com.zanoccio.axirassa.domain;

import org.testng.annotations.Test;

import com.zanoccio.axirassa.domain.User;

public class UserTest {

	@Test
	public void createUsers() {
		User user = new User();
		user.setEMail("foo@place.com");
		user.setName("foo duuude");
		user.setPassword("blah");

	}

}
