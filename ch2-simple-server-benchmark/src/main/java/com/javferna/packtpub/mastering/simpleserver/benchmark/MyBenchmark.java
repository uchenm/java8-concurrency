/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.javferna.packtpub.mastering.simpleserver.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import com.javferna.packtpub.mastering.simpleserver.common.Constants;
import com.javferna.packtpub.mastering.simpleserver.concurrent.client.ConcurrentClient;
import com.javferna.packtpub.mastering.simpleserver.serial.client.SerialClient;
import com.javferna.packtpub.mastering.simpleserver.wdi.data.WDIDAO;

@State(Scope.Benchmark)
public class MyBenchmark {

	@Param({"5"})
	private int numClients;
	
	private WDIDAO dao=WDIDAO.getDAO();
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serialClients() {
		System.out.println("Serial: Number of Simultaneous Clients: "+numClients);
		Thread[] threads= new Thread[numClients];
		SerialClient client=new SerialClient(dao);
		for (int j=0; j<numClients; j++) {
			threads[j] = new Thread(client);
			threads[j].start();
		}
		
		for (int j=0; j<numClients; j++) {
			try {
				threads[j].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, time = 1, batchSize = 1)
	@Measurement(iterations = 10, time = 1, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void concurrentClients() {
		System.out.println("Concurrent: Number of Simultaneous Clients: "+numClients);
		Thread[] threads= new Thread[numClients];
		ConcurrentClient client=new ConcurrentClient(dao);
		for (int j=0; j<numClients; j++) {
			threads[j] = new Thread(client);
			threads[j].start();
		}
		
		for (int j=0; j<numClients; j++) {
			try {
				threads[j].join();
			} catch (InterruptedException e) {
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

			StringWriter writer = new StringWriter();
			writer.write("s");
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/*
	@TearDown(Level.Trial)
	public void shutdownSerial() {
		System.out.println("Shutting down serial server");
		try (Socket echoSocket = new Socket("localhost",
				Constants.SERIAL_PORT);
				PrintWriter out = new PrintWriter(
						echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in))) {

			StringWriter writer = new StringWriter();
			writer.write("z");
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println("OUTPUT: "+output);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@TearDown(Level.Trial)
	public void shutdownConcurrent() {
		System.out.println("Shutting down concurrent server");
		try (Socket echoSocket = new Socket("localhost",
				Constants.CONCURRENT_PORT);
				PrintWriter out = new PrintWriter(
						echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in))) {

			StringWriter writer = new StringWriter();
			writer.write("z");
			writer.write(";");

			String command = writer.toString();
			out.println(command);
			String output = in.readLine();
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	*/
}
