package axirassa.dao;


import axirassa.model.UserEntity;


/**
 * Created by IntelliJ IDEA. User: wiktor Date: 3/22/11 Time: 12:33 PM To change this template use File | Settings |
 * File Templates.
 */
public interface UserDAO {
	boolean isEmailRegistered (String email);
	UserEntity getUserByEmail (String email);
}
