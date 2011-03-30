package axirassa.dao;

import java.util.List;

import axirassa.model.UserEntity;
import axirassa.model.UserPhoneNumberEntity;

public interface UserPhoneNumberDAO {

    public abstract List<UserPhoneNumberEntity> getPhoneNumbersByUser (UserEntity user);


    public abstract UserPhoneNumberEntity getByIdWithUser (long id);


}
