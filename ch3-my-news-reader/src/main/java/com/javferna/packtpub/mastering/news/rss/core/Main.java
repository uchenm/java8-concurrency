package com.javferna.packtpub.mastering.news.rss.core;

import java.util.concurrent.TimeUnit;

import com.javferna.packtpub.mastering.news.rss.reader.basic.NewsSystem;

/**
 * Main class of the basic example
 * @author author
 *
 */
public class Main {

	public static void main(String[] args) {
		
		
		// Creates the System an execute it as a Thread
		NewsSystem system=new NewsSystem("data\\sources.txt");
		
		Thread t=new Thread(system);
		
		t.start();
		
		// Waits 10 minutes
		try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Shutsdown the system
		system.shutdown();

	}

}
