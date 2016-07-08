package com.javferna.packtpub.mastering.advancedServer.serial.command;

import com.javferna.packtpub.mastering.advancedServer.common.Command;

/**
 * Class that implements the serial version of the Error command. It's executed
 * when an unknown command arrives
 * @author author
 *
 */
public class ErrorCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ErrorCommand(String[] command) {
		super(command);
	}
	
	@Override
	/**
	 * Method that executes the command
	 */
	public String execute() {
		return "Unknown command: "+command[0];
	}

}
