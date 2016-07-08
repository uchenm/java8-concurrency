package com.javferna.packtpub.book.mastering.textcategorization.reader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.javferna.packtpub.book.mastering.textcategorization.common.TextFile;

public class DocumentReader implements Runnable {

	private ConcurrentLinkedDeque<String> files;
	private ConcurrentLinkedQueue<TextFile> buffer;
	private CountDownLatch readersCounter;

	public DocumentReader(ConcurrentLinkedDeque<String> files,
			ConcurrentLinkedQueue<TextFile> buffer,
			CountDownLatch readersCounter) {
		this.files = files;
		this.buffer = buffer;
		this.readersCounter = readersCounter;
	}

	@Override
	public void run() {

		String route;
		System.out.println(Thread.currentThread().getName()+": Reader start");

		while ((route = files.pollFirst()) != null) {
			Path file = Paths.get(route);
			
			TextFile textFile;
			try {
				textFile = new TextFile(file);
				buffer.offer(textFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println(Thread.currentThread().getName()+": Reader end: "+buffer.size());
		readersCounter.countDown();
	}
}
