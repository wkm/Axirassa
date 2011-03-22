
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
import axirassa.dao.UserDAO;
import axirassa.dao.UserDAOImpl;
import axirassa.dao.UserEmailAddressDAO;
import axirassa.dao.UserEmailAddressDAOImpl;

@SubModule({ HibernateCoreModule.class })
public class DAOModule {
	public static void bind (ServiceBinder binder) {
		binder.bind(UserDAO.class, UserDAOImpl.class);
		binder.bind(FeedbackDAO.class, FeedbackDAOImpl.class);
		binder.bind(PasswordResetTokenDAO.class, PasswordResetTokenDAOImpl.class);
		binder.bind(PingerDAO.class, PingerDAOImpl.class);
		binder.bind(UserEmailAddressDAO.class, UserEmailAddressDAOImpl.class);
	}


	@Match("*DAO")
	public static void adviseTransactions (HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}

}
