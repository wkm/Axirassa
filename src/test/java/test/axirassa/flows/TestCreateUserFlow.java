package test.axirassa.flows;

import axirassa.ioc.IocTestRunner;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(IocTestRunner.class)
public class TestCreateUserFlow {

    @Inject
    private CreateUserFlow createUserFlow;

    @Inject
    private Session database;

    @Test
    public void createUser () {
        createUserFlow.setEmail("who@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();

        Query query = database.createQuery("from UserEntity");
        List<UserEntity> users = query.list();

        assertFalse(users.isEmpty());
    }


}
