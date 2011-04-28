
package axirassa.ioc

import org.apache.tapestry5.ioc.ScopeConstants
import org.apache.tapestry5.ioc.annotations.Scope
import org.apache.tapestry5.ioc.services.PerthreadManager
import org.apache.tapestry5.ioc.services.PropertyShadowBuilder
import org.hornetq.api.core.HornetQException

import axirassa.webapp.services.EmailNotifyService
import axirassa.webapp.services.MessagingSession
import axirassa.webapp.services.MessagingSessionManager
import axirassa.webapp.services.SmsNotifyService
import axirassa.webapp.services.VoiceNotifyService
import axirassa.webapp.services.EmailNotifyServiceImpl
import axirassa.webapp.services.MessagingSessionManagerImpl
import axirassa.webapp.services.SmsNotifyServiceImpl
import axirassa.webapp.services.VoiceNotifyServiceImpl

/**
 *
 * @author wiktor
 */
class MessagingModule {

    @Scope(ScopeConstants.PERTHREAD)
    def buildMessagingSessionManager(perthreadManager : PerthreadManager) {
        val sessionManager = new MessagingSessionManagerImpl()
        perthreadManager.addThreadCleanupListener(sessionManager)

        sessionManager
    }

    @Scope(ScopeConstants.PERTHREAD)
    def buildMessagingSession(sessionManager : MessagingSessionManager, propertyShadowBuilder : PropertyShadowBuilder) =
        propertyShadowBuilder.build(sessionManager, "session", classOf[MessagingSession])

    @Scope(ScopeConstants.PERTHREAD)
    def buildEmailNotifyService(messagingSession : MessagingSession) =
        new EmailNotifyServiceImpl(messagingSession)

    @Scope(ScopeConstants.PERTHREAD)
    def buildSmsNotifyService(messagingSession : MessagingSession) =
        new SmsNotifyServiceImpl(messagingSession)

    @Scope(ScopeConstants.PERTHREAD)
    def buildVoiceNotifyService(messagingSession : MessagingSession) =
        new VoiceNotifyServiceImpl(messagingSession)

}
