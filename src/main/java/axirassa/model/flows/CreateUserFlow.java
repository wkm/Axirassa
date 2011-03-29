
package axirassa.model.flows;

/**
 * Create a user by
 * 
 * @author wiktor
 */
public interface CreateUserFlow extends Flow {

	public abstract void setPassword (String password);

	public abstract void setEmail (String email);

}
