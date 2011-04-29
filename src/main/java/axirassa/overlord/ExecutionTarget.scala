
package axirassa.overlord

object ExecutionTarget {
	def canonicalizeName(name : String) = name.toLowerCase
}

class ExecutionTarget(var name : String, var className : String) {
	
	var targetClass : Class[_] = _
	var options = new ExecutionTargetOptions
	var autoRestart = false

	if (!OverlordUtilities.isValidName(name))
			throw new InvalidOverlordNameException(name)
	
	targetClass = Class.forName(className)


	/**
	 * @return the canonical name of this execution target typically the
	 *         {@link #getName() name} in lower case.
	 * 
	 *         The canonical name must be unique within the configuration.
	 */
	def getCanonicalName = ExecutionTarget.canonicalizeName(name)
}
