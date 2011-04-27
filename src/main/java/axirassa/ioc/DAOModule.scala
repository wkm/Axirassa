
package axirassa.ioc

import org.apache.tapestry5.hibernate.HibernateCoreModule
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor
import org.apache.tapestry5.ioc.MethodAdviceReceiver
import org.apache.tapestry5.ioc.ServiceBinder
import org.apache.tapestry5.ioc.annotations.Match
import org.apache.tapestry5.ioc.annotations.SubModule

import axirassa.dao.FeedbackDAO
import axirassa.dao.FeedbackDAOImpl
import axirassa.dao.PasswordResetTokenDAO
import axirassa.dao.PasswordResetTokenDAOImpl
import axirassa.dao.PingerDAO
import axirassa.dao.PingerDAOImpl
import axirassa.dao.UserDAO
import axirassa.dao.UserDAOImpl
import axirassa.dao.UserEmailAddressDAO
import axirassa.dao.UserEmailAddressDAOImpl
import axirassa.dao.UserPhoneNumberDAO
import axirassa.dao.UserPhoneNumberDAOImpl

@SubModule(Array(classOf[HibernateCoreModule]))
class DAOModule {
	def bind (binder: ServiceBinder) {
		binder.bind(classOf[UserDAO], classOf[UserDAOImpl])
		binder.bind(classOf[FeedbackDAO], classOf[FeedbackDAOImpl])
		binder.bind(classOf[PasswordResetTokenDAO], classOf[PasswordResetTokenDAOImpl])
		binder.bind(classOf[PingerDAO], classOf[PingerDAOImpl])
		binder.bind(classOf[UserEmailAddressDAO], classOf[UserEmailAddressDAOImpl])
		binder.bind(classOf[UserPhoneNumberDAO], classOf[UserPhoneNumberDAOImpl])
	}


	@Match(Array("*DAO"))
	def adviseTransactions ( advisor: HibernateTransactionAdvisor,  receiver: MethodAdviceReceiver) {
		advisor.addTransactionCommitAdvice(receiver)
	}

}
