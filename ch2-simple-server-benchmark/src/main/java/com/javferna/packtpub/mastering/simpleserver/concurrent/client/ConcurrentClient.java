package com.javferna.packtpub.mastering.simpleserver.concurrent.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import com.javferna.packtpub.mastering.simpleserver.common.Constants;
import com.javferna.packtpub.mastering.simpleserver.wdi.data.WDI;
import com.javferna.packtpub.mastering.simpleserver.wdi.data.WDIDAO;

/**
 * Class that implements a client of the concurrent server. It implements the
 * Runnable interface. It will executed as an independent thread
 * @author author
 *
 */
public class ConcurrentClient implements Runnable{

	private WDIDAO dao;
	
	public ConcurrentClient(WDIDAO dao) {
		this.dao=dao;
	}
	
	public void run() {
		List<WDI> data = dao.getData();
		Random randomGenerator = new Random();

		for (int i = 0; i < 10; i++) {

			for (int j = 0; j < 9; j++) {
				try (Socket echoSocket = new Socket("localhost",
						Constants.CONCURRENT_PORT);
						PrintWriter out = new PrintWriter(
								echoSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(
								new InputStreamReader(
										echoSocket.getInputStream()));
						BufferedReader stdIn = new BufferedReader(
								new InputStreamReader(System.in))) {
					WDI wdi = data.get(randomGenerator.nextInt(data.size()));

					StringWriter writer = new StringWriter();
					writer.write("q");
					writer.write(";");
					writer.write(wdi.getCountryCode());
					writer.write(";");
					writer.write(wdi.getIndicatorCode());

					String command = writer.toString();
					out.println(command);
					String output = in.readLine();
					System.err.println("OUTPUT: "+output);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try (Socket echoSocket = new Socket("localhost",
					Constants.CONCURRENT_PORT);
					PrintWriter out = new PrintWriter(
							echoSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(echoSocket.getInputStream()));
					BufferedReader stdIn = new BufferedReader(
							new InputStreamReader(System.in))) {
				WDI wdi = data.get(randomGenerator.nextInt(data.size()));

				StringWriter writer = new StringWriter();
				writer.write("r");
				writer.write(";");
				writer.write(wdi.getIndicatorCode());

				String command = writer.toString();
				out.println(command);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}


}
