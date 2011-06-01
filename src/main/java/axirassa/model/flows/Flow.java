
package axirassa.model.flows;

/**
 * {@link Flow Flows} are abstractions encoding non-trivial business logic,
 * typically around DAOs.
 * 
 * @author wiktor
 */
public interface Flow {
	public void execute() throws Exception;
}
