package com.javferna.packtpub.mastering.advancedServer.serial.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.javferna.packtpub.mastering.advancedServer.common.Command;
import com.javferna.packtpub.mastering.advancedServer.common.Constants;
import com.javferna.packtpub.mastering.advancedServer.serial.command.ErrorCommand;
import com.javferna.packtpub.mastering.advancedServer.serial.command.QueryCommand;
import com.javferna.packtpub.mastering.advancedServer.serial.command.ReportCommand;
import com.javferna.packtpub.mastering.advancedServer.serial.command.StopCommand;
import com.javferna.packtpub.mastering.advancedServer.wdi.data.WDIDAO;

/**
 * Class that implements the serial server.
 * @author author
 *
 */
public class SerialServer {

	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		WDIDAO dao=WDIDAO.getDAO();
		boolean stopServer=false;
		System.out.println("Initialization completed.");
		
		try {
			serverSocket= new ServerSocket(Constants.SERIAL_PORT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		do {
			try (Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(
							clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));) {
				String line = in.readLine();
				Command command;
				
				String[] commandData=line.split(";");
				System.out.println("Command: "+commandData[0]);
				if (commandData[0].equals("q")) {
					System.out.println("Query");
					command=new QueryCommand(commandData);
				} else if (commandData[0].equals("r")) {
					System.out.println("Report");
					command=new ReportCommand(commandData);
				} else if (commandData[0].equals("z")) {
					System.out.println("Stop");
					command=new StopCommand(commandData);
					stopServer=true;
				} else {
					System.out.println("Error");
					command=new ErrorCommand(commandData);
				} 
				String response=command.execute();
				System.out.println(response);
				out.println(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!stopServer);
	}

}
