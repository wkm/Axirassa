package axirassa.model.flows;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;

/**
 * Create a user and notify their e-mail of the user's creation.
 *
 * @author wiktor
 */
trait CreateUserFlow extends Flow {

    def setPassword(password : String)
    def setEmail(email : String)

    def getUserEntity : UserEntity
    def getPrimaryEmailEntity : UserEmailAddressEntity
}
