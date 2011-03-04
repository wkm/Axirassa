
package axirassa.model;

import axirassa.webapp.services.AxirassaSecurityService;

/**
 * Represents an entity that can directly query for the user. This forms an
 * integral part of the Axirassa security model. See
 * {@link AxirassaSecurityService#verifyOwnership(EntityWithUser)}.
 * 
 * @author wiktor
 */
public interface EntityWithUser {

	public UserEntity getUser();

}
