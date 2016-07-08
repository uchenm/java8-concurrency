package com.javferna.packtpub.book.mastering.textcategorization.indexer;

import java.text.Normalizer;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.javferna.packtpub.book.mastering.textcategorization.common.Document;
import com.javferna.packtpub.book.mastering.textcategorization.common.TextFile;
import com.javferna.packtpub.book.mastering.textcategorization.common.Vocabulary;

public class Indexer implements Runnable {

	
	private ConcurrentLinkedQueue<TextFile> buffer;
	private ConcurrentLinkedDeque<Document> documents;
	private CountDownLatch readersCounter;
	private CountDownLatch indexersCounter;
	private Vocabulary voc;
	
	public Indexer(ConcurrentLinkedDeque<Document> documents, ConcurrentLinkedQueue<TextFile> buffer,CountDownLatch readersCounter,CountDownLatch indexersCounter, Vocabulary voc) {
		this.buffer=buffer;
		this.readersCounter=readersCounter;
		this.indexersCounter=indexersCounter;
		this.documents=documents;
		this.voc=voc;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+": Indexer start");
		do {
			TextFile textFile= buffer.poll();
			if (textFile!=null) {
				Document document= parseDoc(textFile);
				document.getVoc().values()
					.forEach(voc::addWord);
				documents.offer(document);
			}
		} while ((readersCounter.getCount()>0) || (!buffer.isEmpty()));
		indexersCounter.countDown();
		System.out.println(Thread.currentThread().getName()+": Indexer end");
	}

	
	private Document parseDoc(TextFile textFile) {
		Document doc = new Document();
		doc.setName(textFile.getFileName());
		textFile.getContent().forEach(line -> parseLine(line,doc));

		return doc;
	}

	private static void parseLine(String inputLine, Document doc) {

		// Clean string
		String line=new String(inputLine);
		line = Normalizer.normalize(line, Normalizer.Form.NFKD);
		line = line.replaceAll("[^\\p{ASCII}]", "");
		line = line.toLowerCase();

		// Tokenizar
		StringTokenizer tokenizer = new StringTokenizer(line,
				" ,.;:-{}[]¿?¡!|\\=*+/()\"@\t~#<>", false);
		while (tokenizer.hasMoreTokens()) {
			doc.addWord(tokenizer.nextToken());
		}
	}	

}
