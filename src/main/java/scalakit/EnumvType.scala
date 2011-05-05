package scalakit


import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import org.hibernate.usertype.UserType

class EnumvType (val et: Enumeration) extends Enumeration with UserType {
  val SQL_TYPES = Array(Types.VARCHAR)
  override def sqlTypes = SQL_TYPES

  override def returnedClass =
    classOf[et.Value]

  override def equals (x: Object, y: Object) =
    x == y

  override def hashCode (x: Object) : Int =
    x.hashCode

  override def nullSafeGet (resultSet: ResultSet, names: Array[String], owner: Object) = {
    val value = resultSet.getString(names(0))
    if (resultSet.wasNull())
      null
    else
      et.withName(value)
  }

  override def nullSafeSet (statement: PreparedStatement, value: Object, index: Int) =
    if (value == null)
      statement.setNull(index, Types.VARCHAR)
    else
      statement.setString(index, value.toString)

  override def deepCopy (value: Object) =
    value

  override def isMutable =
    false

  override def disassemble (value: Object) =
    value.asInstanceOf[Serializable]

  override def assemble (cached: Serializable, owner: Object) =
    cached

  override def replace (original: Object, target: Object, owner: Object) =
    original
}