package axirassa.model

object UserRole extends Enumeration {
    val OWNER = Value("O")
    val ADMIN = Value("A")
    val BILLING = Value("B")
    val TECHNICAL = Value("T")
    val VIEWER = Value("V")
}