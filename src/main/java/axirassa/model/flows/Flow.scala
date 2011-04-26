
package axirassa.model.flows;

/**
 * {@link Flow Flows} are abstractions encoding non-trivial business logic,
 * typically around DAO.
 *
 * @author wiktor
 */
trait Flow {
    def execute()
}
