
package axirassa.overlord

import java.util.LinkedHashMap

class OverlordConfiguration(var overlord : Overlord) {
	val targets = new LinkedHashMap[String, ExecutionTarget]
	val groups = new LinkedHashMap[String, ExecutionGroup]

	var baseDirectory : String = null
	var classPath : String = null
	var javaExecutable : String = null

	def addExecutionGroup(group : ExecutionGroup) {
		groups.put(group.getCanonicalName, group)
	}

	def hasExecutionGroup(name : String) = (getExecutionGroup(name) != null)

	def getExecutionGroup(name : String) =
		groups.get(ExecutionGroup.canonicalizeName(name))

	def addExecutionTarget(target : ExecutionTarget) = targets.put(target.getCanonicalName, target)

	def hasExecutionTarget(name : String) =
		(getExecutionTarget(name) != null)

	def getExecutionTarget(name : String) = targets.get(ExecutionTarget.canonicalizeName(name))
}
