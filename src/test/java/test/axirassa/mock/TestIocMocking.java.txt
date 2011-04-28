package test.axirassa.mock;

import axirassa.webapp.services.EmailNotifyService;
import axirassa.ioc.IocUnitTestRunner;
import axirassa.services.email.EmailTemplate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 *
 * @author wiktor
 */
@RunWith(IocUnitTestRunner.class)
public class TestIocMocking {

    @Inject
    private Session database;

    @Inject
    private EmailNotifyService emailer;

    @Test
    public void testMocking () {
        // test
        database.beginTransaction();
        database.beginTransaction();
        emailer.startMessage(EmailTemplate.AGGREGATED_FEEDBACK);

        // verify
        verify(database, times(2)).beginTransaction();
        verify(emailer).startMessage(EmailTemplate.AGGREGATED_FEEDBACK);
    }


}
