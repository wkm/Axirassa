package test.axirassa.flows;

import axirassa.webapp.services.EmailNotifyService;
import axirassa.ioc.IocUnitTestRunner;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;
import axirassa.services.email.EmailTemplate;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

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
        createUserFlow.setEmail("who@foo.com");
        createUserFlow.setPassword("password");
        createUserFlow.execute();

        verify(emailer).startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
        verify(emailer).setToAddress("who@foo.com");
        verify(emailer).addAttribute("axlink", "https://axirassa/");
    }


}
