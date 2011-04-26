package axirassa.model.exception

import axirassa.model.UserEntity

class NoSaltException(user: UserEntity) extends Exception("Cannot hash password without a salt for " + user)