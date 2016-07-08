package com.javferna.packtpub.book.mastering.textcategorization.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.javferna.packtpub.book.mastering.textcategorization.clustering.ClusterTask;
import com.javferna.packtpub.book.mastering.textcategorization.common.Document;
import com.javferna.packtpub.book.mastering.textcategorization.common.TextFile;
import com.javferna.packtpub.book.mastering.textcategorization.common.Vocabulary;
import com.javferna.packtpub.book.mastering.textcategorization.indexer.Indexer;
import com.javferna.packtpub.book.mastering.textcategorization.mapper.Mapper;
import com.javferna.packtpub.book.mastering.textcategorization.reader.DocumentReader;

public class ClusteringDocs {
	
	private static int NUM_READERS = 2;
	private static int NUM_WRITERS = 4;

	public static void main(String[] args) throws InterruptedException {
		
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
		ConcurrentLinkedDeque<String> files=readFileNames("data");
		System.out.println(new Date()+":"+files.size()+" files read.");

		ConcurrentLinkedQueue<TextFile> buffer=new ConcurrentLinkedQueue<>();
		CountDownLatch readersCounter=new CountDownLatch(2);
		ConcurrentLinkedDeque<Document> documents=new ConcurrentLinkedDeque<>();
		CountDownLatch indexersCounter=new CountDownLatch(4);
		Vocabulary voc=new Vocabulary();
		
		
		System.out.println(new Date()+":"+"Launching the tasks");
		for (int i=0; i<NUM_READERS; i++) {
			DocumentReader reader=new DocumentReader(files,buffer,readersCounter);
			executor.execute(reader);
			
		}
		
		for (int i=0; i<NUM_WRITERS; i++) {
			Indexer indexer=new Indexer(documents, buffer, readersCounter, indexersCounter, voc);
			executor.execute(indexer);
		}
		
		System.out.println(new Date()+":"+"Waiting for the readers");
		readersCounter.await();
		
		System.out.println(new Date()+":"+"Waiting for the indexers");
		indexersCounter.await();
		
		Document[] documentsArray=new Document[documents.size()];
		documentsArray=documents.toArray(documentsArray);
		
		System.out.println(new Date()+":"+"Launching the mappers");
		
		CompletableFuture<Void>[] completables = Stream.generate(() -> new Mapper(documents, voc))
			    .limit(4)
			    .map(CompletableFuture::runAsync)
			    .toArray(CompletableFuture[]::new);
		
		System.out.println(new Date()+":"+"Launching the cluster calculation");

		CompletableFuture<Void> completableMappers=CompletableFuture.allOf(completables);
		ClusterTask clusterTask=new ClusterTask(documentsArray, voc);
		CompletableFuture<Void> completableClustering=completableMappers.thenRunAsync(clusterTask);
		
		System.out.println(new Date()+":"+"Wating for the cluster calculation");		
		try {
			completableClustering.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println(new Date()+":"+"Execution finished");
		executor.shutdown();

	}

	private static ConcurrentLinkedDeque<String> readFileNames(String path) {
		
		try {
			return Files.list(Paths.get(path))
				    .map(Path::toString)
				    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
 