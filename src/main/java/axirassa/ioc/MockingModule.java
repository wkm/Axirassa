package axirassa.ioc;

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
    // DATABASE MOCKS
    public static Session decorateSession() {
        return mock(Session.class);
    }

    // MESSAGING MOCKS
    public static MessagingSessionManager decorateMessagingSessionManager() {
        return mock(MessagingSessionManager.class);
    }

    public static MessagingSession decorateMessagingSession() {
        return mock(MessagingSession.class);
    }

    public static EmailNotifyService decorateEmailNotifyService() {
        return mock(EmailNotifyService.class);
    }

    public static SmsNotifyService decorateSmsNotifyService() {
        return mock(SmsNotifyService.class);
    }

    public static VoiceNotifyService decorateVoiceNotifyService() {
        return mock(VoiceNotifyService.class);
    }
}
