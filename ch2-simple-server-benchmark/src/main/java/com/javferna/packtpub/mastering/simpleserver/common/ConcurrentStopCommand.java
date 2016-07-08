package com.javferna.packtpub.mastering.simpleserver.common;

import com.javferna.packtpub.mastering.simpleserver.concurrent.server.ConcurrentServer;

/**
 * Class that implements the concurrent version of the Stop command.
 * Stops the server
 * @author author
 *
 */
public class ConcurrentStopCommand extends Command {

	/**
	 * Constructor of the class
	 * @param command String that represents the command
	 */
	public ConcurrentStopCommand (String [] command) {
		super (command);
		setCacheable(false);
	}
	
	@Override
	/**
	 * Method that executes the command
	 */
	public String execute() {
		ConcurrentServer.shutdown();
		return "Server stopped";
	}

}
