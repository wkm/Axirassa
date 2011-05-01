
package axirassa.overlord

import java.io.IOException
import java.util.ArrayList

import scala.collection.JavaConversions._

object ExecutionGroup {
	def canonicalizeName(name : String) = name.toLowerCase
}

class ExecutionGroup(var name : String) {
	val executionSpecifications = new ArrayList[ExecutionSpecification]

	if (!OverlordUtilities.isValidName(name))
		throw new InvalidOverlordNameException(name)

	def getCanonicalName = name.toLowerCase

	def addExecutionSpecification(spec : ExecutionSpecification) {
		executionSpecifications.add(spec)
	}

	def execute() = {
		executionSpecifications.map(spec => spec.execute)
	}

}
