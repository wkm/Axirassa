
package axirassa.ioc;

import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;

import axirassa.dao.FeedbackDAO;
import axirassa.dao.FeedbackDAOImpl;
import axirassa.dao.PasswordResetTokenDAO;
import axirassa.dao.PasswordResetTokenDAOImpl;
import axirassa.dao.PingerDAO;
import axirassa.dao.PingerDAOImpl;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.dao.UserEmailAddressDAOImpl;
import axirassa.dao.UserPhoneNumberDAO;
import axirassa.dao.UserPhoneNumberDAOImpl;

@SubModule({ HibernateCoreModule.class })
public class DAOModule {

	/**
	 * DAO bindings listed here should also be added to {@link DAOMockingModule}
	 */
	public static void bind(ServiceBinder binder) {
		binder.bind(FeedbackDAO.class, FeedbackDAOImpl.class);
		binder.bind(PasswordResetTokenDAO.class, PasswordResetTokenDAOImpl.class);
		binder.bind(PingerDAO.class, PingerDAOImpl.class);
		binder.bind(UserEmailAddressDAO.class, UserEmailAddressDAOImpl.class);
		binder.bind(UserPhoneNumberDAO.class, UserPhoneNumberDAOImpl.class);
	}


	@Match("*DAO")
	public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

}
