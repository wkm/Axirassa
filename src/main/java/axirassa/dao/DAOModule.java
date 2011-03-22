package axirassa.dao;


import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;


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
