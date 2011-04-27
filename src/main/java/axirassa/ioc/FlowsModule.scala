
package axirassa.ioc

import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor
import org.apache.tapestry5.ioc.MethodAdviceReceiver
import org.apache.tapestry5.ioc.ServiceBinder
import org.apache.tapestry5.ioc.annotations.Match
import org.apache.tapestry5.ioc.annotations.SubModule

import axirassa.model.flows.CreateUserFlow
import axirassa.model.flows.CreateUserFlowImpl

@SubModule(Array(classOf[DAOModule]))
class FlowsModule {
	def bind (binder : ServiceBinder ) {
		binder.bind(classOf[CreateUserFlow], classOf[CreateUserFlowImpl])
	}


	@Match(Array("*Flow"))
	def adviseTransactions (advisor:HibernateTransactionAdvisor ,receiver: MethodAdviceReceiver ) {
		advisor.addTransactionCommitAdvice(receiver)
	}
}
