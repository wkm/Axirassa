
package test.axirassa.domain;

import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.AccountEntity;

@RunWith(IocIntegrationTestRunner.class)
public class TestAccountEntity {
	@Test
	public void accounts () {
		AccountEntity account = new AccountEntity();
	}
}
