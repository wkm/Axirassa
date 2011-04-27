
package axirassa.ioc

import org.mockito.Matchers.any
import org.mockito.Matchers.anyObject
import org.mockito.Mockito.mock
import org.mockito.Mockito.when

import org.apache.tapestry5.Link
import org.apache.tapestry5.ioc.MappedConfiguration
import org.apache.tapestry5.services.PageRenderLinkSource
import org.hibernate.Session

import axirassa.webapp.services.EmailNotifyService
import axirassa.webapp.services.MessagingSession
import axirassa.webapp.services.MessagingSessionManager
import axirassa.webapp.services.SmsNotifyService
import axirassa.webapp.services.VoiceNotifyService

/**
 * Provides mock implementations of common services
 * 
 * @author wiktor
 */
class MockingModule {

	def contributeServiceOverride (configuration : MappedConfiguration[Class[_], Object] ) {
		configuration.add(classOf[Session], mock(classOf[Session]))
		configuration.add(classOf[MessagingSessionManager], mock(classOf[MessagingSessionManager]))
		configuration.add(classOf[MessagingSession], mock(classOf[MessagingSession]))
		configuration.add(classOf[EmailNotifyService], mock(classOf[EmailNotifyService]))
		configuration.add(classOf[SmsNotifyService], mock(classOf[SmsNotifyService]))
		configuration.add(classOf[VoiceNotifyService], mock(classOf[VoiceNotifyService]))

		val mockLink = mock(classOf[Link])
		when(mockLink.toAbsoluteURI()).thenReturn("http://axirassa/")
		when(mockLink.toAbsoluteURI(true)).thenReturn("https://axirassa/")
		when(mockLink.toAbsoluteURI(false)).thenReturn("http://axirassa/")

		val mockPrls = mock(classOf[PageRenderLinkSource])
		when(mockPrls.createPageRenderLinkWithContext(any(classOf[Class[_]]), anyObject())).thenReturn(mockLink)
		configuration.add(classOf[PageRenderLinkSource], mockPrls)
	}

}
