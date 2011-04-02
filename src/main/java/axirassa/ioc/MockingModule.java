package axirassa.ioc;

import org.apache.tapestry5.ioc.MappedConfiguration;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.MessagingSessionManager;
import axirassa.webapp.services.SmsNotifyService;
import axirassa.webapp.services.VoiceNotifyService;
import org.hibernate.Session;
import static org.mockito.Mockito.mock;

/**
 * Provides mock implementations of common services
 *
 * @author wiktor
 */
public class MockingModule {

    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration) {
        configuration.add(Session.class, mock(Session.class));
        configuration.add(MessagingSessionManager.class, mock(MessagingSessionManager.class));
        configuration.add(MessagingSession.class, mock(MessagingSession.class));
        configuration.add(EmailNotifyService.class, mock(EmailNotifyService.class));
        configuration.add(SmsNotifyService.class, mock(SmsNotifyService.class));
        configuration.add(VoiceNotifyService.class, mock(VoiceNotifyService.class));
    }

}
