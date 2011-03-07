
package axirassa.webapp.services;

import org.apache.shiro.subject.Subject;
import org.hibernate.Session;

import axirassa.model.EntityWithUser;
import axirassa.model.UserEntity;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

public interface AxirassaSecurityService {

	public abstract String getEmail();


	/**
	 * Returns the {@link UserEntity} associated with the authenticated user, or
	 * null if the user is not authenticated.
	 */
	public abstract UserEntity getUserEntity(Session database);


	public abstract boolean isUser();


	public abstract boolean isGuest();


	public abstract boolean isAuthenticated();


	public abstract Subject getSubject();


	/**
	 * Throws an {@link AxirassaSecurityException} if the given entity returns
	 * null from {@link EntityWithUser#getUser()} or if the user returned does
	 * not match the user currently logged in.
	 * 
	 * If the given entity is null, no exception is thrown. (good luck getting
	 * any data out of it)
	 * 
	 * @throws AxirassaSecurityException
	 */
	public abstract void verifyOwnership(EntityWithUser entity) throws AxirassaSecurityException;

}
