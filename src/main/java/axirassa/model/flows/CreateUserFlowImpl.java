
package axirassa.model.flows;

import java.io.IOException;

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

	private String email;

	private UserEntity userEntity;

	private UserEmailAddressEntity primaryEmailEntity;


	@Override
	public void setEmail(String email) {
		this.email = email;
	}


	private String password;


	@Override
	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	@CommitAfter
	public void execute() {
		System.out.println("PERSISTING USER");

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
		try {
			emailer.send();
		} catch (HornetQException e) {
			logger.error("Fatal messaging error", e);
		} catch (IOException e) {
			logger.error("Fatal I/O error", e);
		}
	}


	@Override
	public UserEntity getUserEntity() {
		return userEntity;
	}


	@Override
	public UserEmailAddressEntity getPrimaryEmailEntity() {
		return primaryEmailEntity;
	}

}
