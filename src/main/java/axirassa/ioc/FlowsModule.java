
package axirassa.ioc;

import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;

import axirassa.model.flows.AddEmailAddressFlow;
import axirassa.model.flows.AddEmailAddressFlowImpl;
import axirassa.model.flows.CreateUserFlow;
import axirassa.model.flows.CreateUserFlowImpl;
import axirassa.model.flows.ResetPasswordFlow;
import axirassa.model.flows.ResetPasswordFlowImpl;

@SubModule({ DAOModule.class })
public class FlowsModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(CreateUserFlow.class, CreateUserFlowImpl.class);
		binder.bind(AddEmailAddressFlow.class, AddEmailAddressFlowImpl.class);
		binder.bind(ResetPasswordFlow.class, ResetPasswordFlowImpl.class);
	}


	@Match("*Flow")
	public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver) {
		advisor.addTransactionCommitAdvice(receiver);
	}
}
