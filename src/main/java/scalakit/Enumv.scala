package scalakit

import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import org.hibernate.usertype.UserType

trait Enumv {
    this : Enumeration =>
        private var valueMap = scala.collection.mutable.Map[String, Value]()
    
        def Value(name : String) = {
            valueMap.put(name, new Val(name))
            new Val(name)
        }
    
        def valueOf(name : String) =
            valueMap.get(name)
}

abstract class EnumvType(val et : Enumeration with Enumv) extends UserType {
    override def sqlTypes =
        Array(Types.VARCHAR)

    override def returnedClass =
        classOf[et.Value]

    override def equals(x : Object, y : Object) =
        x == y

    override def hashCode(x : Object) =
        x.hashCode

    override def nullSafeGet(resultSet : ResultSet, names : Array[String], owner : Object) = {
        val value = resultSet.getString(names(0))
        if (resultSet.wasNull())
            null
        else
            et.valueOf(value)
    }

    override def nullSafeSet(statement : PreparedStatement, value : Object, index : Int) =
        if (value == null)
            statement.setNull(index, Types.VARCHAR)
        else
            statement.setString(index, value.toString)

    override def deepCopy(value : Object) =
        value

    override def isMutable =
        false

    override def disassemble(value : Object) =
        value.asInstanceOf[Serializable]

    override def assemble(cached : Serializable, owner : Object) =
        cached

    override def replace(original : Object, target : Object, owner : Object) =
        original
}