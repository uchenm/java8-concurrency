package com.javferna.packtpub.mastering.news.rss.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.javferna.packtpub.mastering.news.rss.buffer.NewsBuffer;
import com.javferna.packtpub.mastering.news.rss.data.CommonInformationItem;

/**
 * This class writes the news stored in the buffer to disk
 * @author author
 *
 */
public class NewsWriter implements Runnable {

	/**
	 * The buffer wich is the source of the news
	 */
	private NewsBuffer buffer;
	
	/**
	 * Constructor of the class
	 * @param buffer
	 */
	public NewsWriter(NewsBuffer buffer) {
		this.buffer=buffer;
	}
	
	/**
	 * Read the news from the buffer and writes them to disk
	 */
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				CommonInformationItem item=buffer.get();
				
				Path path=Paths.get("output\\"+item.getFileName());
				
				try (BufferedWriter fileWriter = Files.newBufferedWriter(path,StandardOpenOption.CREATE)) {
					fileWriter.write(item.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (InterruptedException e) {
			//Normal execution
		}
		
	}

}
