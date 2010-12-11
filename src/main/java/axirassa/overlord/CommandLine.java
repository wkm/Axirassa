
package axirassa.overlord;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {

	private String command = null;
	private final List<String> arguments = new ArrayList<String>();


	public CommandLine(String command) {
		this.command = command;
	}


	public String getCommand() {
		return command;
	}


	public void addArgument(String argument) {
		arguments.add(argument);
	}


	public void addArguments(List<String> arguments) {
		this.arguments.addAll(arguments);
	}


	public ArrayList<String> buildCommandLine() {
		ArrayList<String> cli = new ArrayList<String>(1 + arguments.size());

		cli.add(command);
		cli.addAll(arguments);

		return cli;
	}

}
