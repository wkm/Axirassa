
package axirassa.overlord

import java.util.ArrayList
import java.util.List

class CommandLine(var command : String) {
	val arguments = new ArrayList[String]
	
	def addArgument(argument : String) {
		arguments.add(argument)
	}
	
	def addArguments(arguments: List[String]) {
		arguments.addAll(arguments)
	}
	
	def buildCommandLine = {
		val cli = new ArrayList[String](1 + arguments.size)
		
		cli.add(command)
		cli.addAll(arguments)
		
		cli
	}
}
