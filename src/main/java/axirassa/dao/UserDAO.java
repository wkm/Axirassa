package axirassa.dao;

import axirassa.model.UserEntity;

public interface UserDAO {

    boolean isEmailRegistered(String email);

    UserEntity getUserByEmail(String email);
}
