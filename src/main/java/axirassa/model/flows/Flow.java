
package axirassa.model.flows;

/**
 * {@link Flow Flows} are abstractions encoding non-trivial business logic,
 * typically around DAO.
 * 
 * @author wiktor
 */
public interface Flow {
	public void execute ();
}
