
package axirassa.ioc;

import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;

import axirassa.model.flows.CreateUserFlow;
import axirassa.model.flows.CreateUserFlowImpl;

@SubModule({ DAOModule.class })
public class FlowsModule {
	public static void bind (ServiceBinder binder) {
		binder.bind(CreateUserFlow.class, CreateUserFlowImpl.class);
	}


	@Match("*Flow")
	public static void adviseTransactions (HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}
}
