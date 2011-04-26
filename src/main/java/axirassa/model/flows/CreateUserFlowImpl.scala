
package axirassa.model.flows;

import scala.reflect.BeanProperty
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectResource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;
import org.slf4j.Logger;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.pages.user.VerifyEmailUser;
import axirassa.webapp.services.EmailNotifyService;

class CreateUserFlowImpl extends CreateUserFlow {

	@InjectResource
	var logger : Logger = _

	@Inject
	var database : Session = _

	@Inject
	var linkSource : PageRenderLinkSource = _

	@Inject
	var emailer : EmailNotifyService = _

	@BeanProperty
	var email : String = _

	@BeanProperty
	var userEntity : UserEntity = _

	@BeanProperty
	var primaryEmailEntity : UserEmailAddressEntity = _

	@BeanProperty
	var password: String = _

	@CommitAfter
	override
	def execute() = {
		println("PERSISTING USER");

		userEntity = new UserEntity();
		userEntity.createPassword(password);

		primaryEmailEntity = new UserEmailAddressEntity();
		primaryEmailEntity.setEmail(email);
		primaryEmailEntity.setPrimaryEmail(true);
		primaryEmailEntity.setUser(userEntity);

		database.save(userEntity);
		database.save(primaryEmailEntity);

		val link = linkSource.createPageRenderLinkWithContext(classOf[VerifyEmailUser], primaryEmailEntity.getToken())
		        .toAbsoluteURI(true);

		emailer.startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		emailer.setToAddress(email);
		emailer.addAttribute("axlink", link);
		try {
			emailer.send();
		} catch {
		    case e : HornetQException => logger.error("Fatal messaging error", e)
		    case e : IOException => logger.error("Fatal I/O error", e)
		}
	}
}
