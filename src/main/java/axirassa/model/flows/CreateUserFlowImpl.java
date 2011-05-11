
package axirassa.model.flows;

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

public class CreateUserFlowImpl implements CreateUserFlow {

	@InjectResource
	private Logger logger;

	@Inject
	private Session database;

	@Inject
	private PageRenderLinkSource linkSource;

	@Inject
	private EmailNotifyService emailer;

	@Getter
	@Setter
	private String email;

	@Getter
	private UserEntity userEntity;

	@Getter
	private UserEmailAddressEntity primaryEmailEntity;

	@Getter
	@Setter
	private String password;


	@Override
	public void execute() throws HornetQException, IOException {
		userEntity = new UserEntity();
		userEntity.createPassword(password);

		primaryEmailEntity = new UserEmailAddressEntity();
		primaryEmailEntity.setEmail(email);
		primaryEmailEntity.setPrimaryEmail(true);
		primaryEmailEntity.setUser(userEntity);

		database.save(userEntity);
		database.save(primaryEmailEntity);

		String link = linkSource.createPageRenderLinkWithContext(VerifyEmailUser.class, primaryEmailEntity.getToken())
		        .toAbsoluteURI(true);

		emailer.startMessage(EmailTemplate.USER_VERIFY_ACCOUNT);
		emailer.setToAddress(email);
		emailer.addAttribute("axlink", link);
		emailer.send();
	}
}
