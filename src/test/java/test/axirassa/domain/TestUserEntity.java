package test.axirassa.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.AbstractDomainTest;
import org.apache.tapestry5.ioc.annotations.Inject;

@RunWith(IocIntegrationTestRunner.class)
public class TestUserEntity extends AbstractDomainTest {

    @Inject
    private CreateUserFlow createUserFlow;

    @Test
    public void userPassword () throws NoSaltException {
        session.beginTransaction();

        createUserFlow.setEmail("who@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();
        UserEntity user = createUserFlow.getUserEntity();

        assertTrue(user.matchPassword("blah"));
        assertFalse(user.matchPassword("blah "));
        assertFalse(user.matchPassword("blah123"));
        assertFalse(user.matchPassword("tweedle"));

        session.getTransaction().commit();
    }


    @Test
    public void userAutomaticSalting () throws NoSaltException {
        session.beginTransaction();
        UserEntity user = new UserEntity();
        long start = System.currentTimeMillis();
        user.createPassword("password");
        System.out.println("PASSWORD IN: " + (System.currentTimeMillis() - start));
        session.save(user);
        session.getTransaction().commit();
    }


}
