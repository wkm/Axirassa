package test.axirassa.webapp.services;

import axirassa.dao.UserDAO;
import axirassa.ioc.IocIntegrationTestRunner;
import static org.junit.Assert.assertTrue;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import axirassa.model.UserEntity;
import axirassa.model.exception.NoSaltException;
import axirassa.model.flows.CreateUserFlow;
import axirassa.webapp.services.UserAuthenticationInfo;
import axirassa.webapp.services.UserCredentialsMatcher;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(IocIntegrationTestRunner.class)
public class TestUserCredentialsMatcher {

    @Inject
    private Session database;

    @Inject
    private CreateUserFlow createUserFlow;

    @Inject
    private UserDAO userDAO;

    @Before
    public void createUsers () throws NoSaltException {
        database.beginTransaction();

        createUserFlow.setEmail("who1@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();

        createUserFlow.setEmail("who2@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();

        Query q = database.createQuery("from UserEntity");
        System.out.println("Users: " + q.list());
    }


    @Test
    public void testMatcher () {
        UserCredentialsMatcher matcher = new UserCredentialsMatcher(database);
        UserAuthenticationInfo authinfo;

        authinfo = UserAuthenticationInfo.createInfoFromEntity(userDAO.getUserByEmail("who1@foo.com"));

        UsernamePasswordToken token1 = new UsernamePasswordToken("who1@foo.com", "password");
        assertTrue(matcher.doCredentialsMatch(token1, authinfo));
    }
}
