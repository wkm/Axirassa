package axirassa.services

import org.hibernate.FetchMode
import axirassa.util.HibernateTools

import scala.collection.JavaConversions._

object DatabaseValidationService {
    def main(args : Array[String]) = {
        val classes = HibernateTools.getSessionFactory().getAllClassMetadata()
        val database = HibernateTools.getLightweightSession()
        
        println("Deep-validating each type")
        
        classes.values.foreach { classmeta => 
        	println("\t" + classmeta.getEntityName)
        	
        	val criteria = database.createCriteria(classmeta.getEntityName)
        	classmeta.getPropertyNames.foreach { property => 
        	    val classType = classmeta.getPropertyType(property)
        	    
        	    if(classType.isAssociationType)
        	        criteria.setFetchMode(classType.getName, FetchMode.JOIN)
        	}
        	
        	criteria.setMaxResults(0)
        	criteria.list
        	
        	println("\t\tVALID")
        }
    }
}
