package test.axirassa.mock;

import axirassa.webapp.services.EmailNotifyService;
import axirassa.ioc.IocUnitTestRunner;
import axirassa.services.email.EmailTemplate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

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
        System.err.println("DATABASE: " + database);
        database.beginTransaction();
        System.err.println("DATABASE: " + database);
        database.beginTransaction();

        emailer.startMessage(EmailTemplate.AGGREGATED_FEEDBACK);
        System.err.println("EMAILER: "+emailer);
        
        // database should be a mock object
        when(database.save(any())).thenThrow(new ExceptionInInitializerError("saved"));
        database.save("object");
    }


}
