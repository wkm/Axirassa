
package axirassa.webapp.pages.user

import java.io.IOException

import org.apache.shiro.authz.annotation.RequiresUser
import org.apache.tapestry5.annotations.Component
import org.apache.tapestry5.annotations.Property
import org.apache.tapestry5.hibernate.annotations.CommitAfter
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.services.PageRenderLinkSource
import org.hibernate.Session
import org.hornetq.api.core.HornetQException

import axirassa.model.UserEmailAddressEntity
import axirassa.model.UserEntity
import axirassa.services.email.EmailTemplate
import axirassa.webapp.components.AxForm
import axirassa.webapp.components.AxTextField
import axirassa.webapp.services.AxirassaSecurityService
import axirassa.webapp.services.EmailNotifyService
import axirassa.webapp.services.exceptions.AxirassaSecurityException

@RequiresUser
class AddEmailAddressUser {

	@Inject
	var linkSource : PageRenderLinkSource = _

	@Inject
	var security : AxirassaSecurityService= _

	@Inject
	var database : Session = _

	@Inject
	var emailer : EmailNotifyService = _

	@Component
	var form : AxForm = _

	@Component
	var emailConfirmField : AxTextField = _

	@Property
	var email : String = _

	@Property
	var emailConfirm : String = _


	def onValidateFromForm () {
		if (email == null)
			return
		if (emailConfirm == null)
			return

		if (!email.equals(emailConfirm))
			form.recordError(emailConfirmField, "e-mails do not match")
	}


	@CommitAfter
	def onSuccessFromForm () {
		val user = security.getUserEntity

		val emailEntity = new UserEmailAddressEntity()
		emailEntity.setUser(user)
		emailEntity.setEmail(email)
		database.save(emailEntity)

		val link = linkSource.createPageRenderLinkWithContext(classOf[VerifyEmailUser], emailEntity.getToken())
		        .toAbsoluteURI(true)

		emailer.startMessage(EmailTemplate.USER_VERIFY_ACCOUNT)
		emailer.setToAddress(email)
		emailer.addAttribute("axlink", link)
		emailer.send()

		classOf[SettingsUser]
	}
}
