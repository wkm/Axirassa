package axirassa.services


import org.hibernate.FetchMode
import axirassa.util.HibernateTools

import scala.collection.JavaConversions._


object DatabaseValidationService {
  def main (args: Array[String]) = {
    val classes = HibernateTools.getSessionFactory().getAllClassMetadata()
    val database = HibernateTools.getLightweightSession()

    println("Deep-validating each type")

    classes.values.foreach {
      classMeta =>
        println("\t" + classMeta.getEntityName)

        val criteria = database.createCriteria(classMeta.getEntityName)
        classMeta.getPropertyNames.foreach {
          property =>
           val classType = classMeta.getPropertyType(property)
           if (classType.isAssociationType)
             criteria.setFetchMode(classType.getName, FetchMode.JOIN)
        }
    
        criteria.setMaxResults(0)
        criteria.list

        println("\t\tVALID")
   }
  }
}
