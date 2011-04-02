package test.axirassa.domain;

import axirassa.dao.PasswordResetTokenDAO;
import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.PasswordResetTokenEntity;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.util.AbstractDomainTest;
import org.apache.tapestry5.ioc.annotations.Inject;

@RunWith(IocIntegrationTestRunner.class)
public class TestPasswordResetTokenEntity extends AbstractDomainTest {

    @Inject
    private PasswordResetTokenDAO passwordResetTokenDAO;
    
    @Inject
    private CreateUserFlow createUserFlow;

    @Test
    public void testAutoGeneration () {
        session.beginTransaction();
        
        
        createUserFlow.setEmail("who@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();
        
        UserEntity user = createUserFlow.getUserEntity();
        UserEmailAddressEntity emailAddress = createUserFlow.getPrimaryEmailEntity();

        PasswordResetTokenEntity token1 = new PasswordResetTokenEntity();
        token1.setUser(user);
        session.save(token1);

        PasswordResetTokenEntity token2 = new PasswordResetTokenEntity();
        token2.setUser(user);
        session.save(token2);

        String tok1 = token1.getToken();
        String tok2 = token2.getToken();

        session.getTransaction().commit();

        token1 = passwordResetTokenDAO.getByToken(tok1);
        token2 = passwordResetTokenDAO.getByToken(tok2);
    }


}
