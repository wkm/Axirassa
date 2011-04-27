package axirassa.model

import scalakit.EnumvType
import scalakit.Enumv

object UserRole extends Enumv {
	val OWNER = Value("O")
	val ADMIN = Value("A")
	val BILLING = Value("B")
	val TECHNICAL = Value("T")
	val VIEWER = Value("V")
}

class UserRoleType extends EnumvType(UserRole)