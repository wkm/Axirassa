package test.axirassa.flows;

import axirassa.webapp.services.EmailNotifyService;
import axirassa.ioc.IocUnitTestRunner;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.overlord.exceptions.ExceptionInMonitorError;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(IocUnitTestRunner.class)
public class TestCreateUserFlow {

    @Inject
    private CreateUserFlow createUserFlow;

    @Inject
    private Session database;

    @Inject
    private EmailNotifyService emailer;

    @Test
    public void createUser () {
        System.out.println("SESSION: "+database);
        when(database.save(any())).thenThrow(new ExceptionInInitializerError("Big PROBLEM"));

        createUserFlow.setEmail("who@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();

        Query query = database.createQuery("from UserEntity");
        List<UserEntity> users = query.list();

        assertFalse(users.isEmpty());
    }


}
