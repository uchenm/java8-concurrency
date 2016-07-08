package com.javferna.packtpub.mastering.advancedServer.concurrent.command;

import java.io.BufferedReader;
import java.net.Socket;

import com.javferna.packtpub.mastering.advancedServer.concurrent.server.ConcurrentServer;
import com.javferna.packtpub.mastering.advancedServer.parallel.log.Logger;

/**
 * Concurrent command that cancel the tasks of a user
 * @author author
 *
 */
public class ConcurrentCancelCommand extends ConcurrentCommand {

	/**
	 * Constructor of the class. It receives the socket to communicate with the client and the parameters of the command
	 * @param socket Socket to communicate with the client
	 * @param command Parameters of the command
	 * @param in The Stream used to read the data from the socket. We have to close it
	 */
	public ConcurrentCancelCommand(Socket socket, String[] command) {
		super(socket, command);
		setCacheable(false);
	}

	/**
	 * Main method of the command. Cancel the tasks of the client and return the message to write to the client
	 */
	@Override
	public String execute() {
		ConcurrentServer.cancelTasks(getUsername());
		String message = "Tasks of user "
				+getUsername()
				+" has been cancelled.";
		Logger.sendMessage(message);
		return message;
	}

}
