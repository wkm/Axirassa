
package axirassa.model.flows;

import java.io.IOException;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.hibernate.Session;
import org.hornetq.api.core.HornetQException;

import lombok.Setter;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.services.email.EmailTemplate;
import axirassa.webapp.pages.user.VerifyEmailUser;
import axirassa.webapp.services.EmailNotifyService;

public class AddEmailAddressFlowImpl implements AddEmailAddressFlow {
	@Setter
	private UserEntity userEntity;

	@Setter
	private String emailAddress;
	
	@Inject
	private Session session;
	
	@Inject
	private EmailNotifyService emailer;
	
	@Inject
	private PageRenderLinkSource linkSource; 
	
	@Override
	@CommitAfter
	public void execute() throws HornetQException, IOException {
		UserEmailAddressEntity emailAddressEntity = new UserEmailAddressEntity();
		emailAddressEntity.setUser(userEntity);
		emailAddressEntity.setEmail(emailAddress);
		emailAddressEntity.setPrimaryEmail(false);
		
		session.save(emailAddressEntity);
		
		String axlink = linkSource.createPageRenderLinkWithContext(VerifyEmailUser.class, emailAddressEntity.getToken()).toAbsoluteURI(true);
		
		emailer.startMessage(EmailTemplate.USER_ADD_EMAIL);
		emailer.setToAddress(emailAddress);
		emailer.addAttribute("axlink", axlink);
		emailer.send();
	}

}
