package axirassa.ioc;

import org.apache.tapestry5.internal.services.LinkImpl;
import org.apache.tapestry5.Link;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.apache.tapestry5.ioc.MappedConfiguration;
import axirassa.webapp.services.EmailNotifyService;
import axirassa.webapp.services.MessagingSession;
import axirassa.webapp.services.MessagingSessionManager;
import axirassa.webapp.services.SmsNotifyService;
import axirassa.webapp.services.VoiceNotifyService;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import static org.mockito.Mockito.*;

/**
 * Provides mock implementations of common services
 *
 * @author wiktor
 */
public class MockingModule {

    public static void contributeServiceOverride (MappedConfiguration<Class, Object> configuration) {
        configuration.add(Session.class, mock(Session.class));
        configuration.add(MessagingSessionManager.class, mock(MessagingSessionManager.class));
        configuration.add(MessagingSession.class, mock(MessagingSession.class));
        configuration.add(EmailNotifyService.class, mock(EmailNotifyService.class));
        configuration.add(SmsNotifyService.class, mock(SmsNotifyService.class));
        configuration.add(VoiceNotifyService.class, mock(VoiceNotifyService.class));

        
        Link mockLink = mock(Link.class);
        when(mockLink.toAbsoluteURI()).thenReturn("http://axirassa/");
        when(mockLink.toAbsoluteURI(true)).thenReturn("https://axirassa/");
        when(mockLink.toAbsoluteURI(false)).thenReturn("http://axirassa/");

        PageRenderLinkSource mockPrls = mock(PageRenderLinkSource.class);
        when(mockPrls.createPageRenderLinkWithContext(any(Class.class), anyObject())).thenReturn(mockLink);
        configuration.add(PageRenderLinkSource.class, mockPrls);
    }


}
