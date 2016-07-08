package com.javferna.packtpub.book.mastering.textcategorization.clustering;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import com.javferna.packtpub.book.mastering.textcategorization.common.Document;

public class ConcurrentKMeans {

	public static DocumentCluster[] calculate(
			Document documents[], int numberClusters, int vocSize, int seed, int minSize) {
		DocumentCluster clusters[] = new DocumentCluster[numberClusters];

		Random random=new Random(seed);
		for (int i = 0; i < numberClusters; i++) {
			clusters[i] = new DocumentCluster(vocSize);
			clusters[i].initialize(random);
		}

		boolean change = true;

		while (change) {
			System.out.println("Step");
			change = assigment(clusters, documents, minSize);
			update(clusters,minSize);
		}
		return clusters;
	}

	private static boolean assigment(DocumentCluster[] clusters,
			Document[] documents, int minSize) {

		boolean change = false;

		for (DocumentCluster cluster : clusters) {
			cluster.clearDocuments();
		}

		AtomicInteger numChanges = new AtomicInteger(0);
		AssigmentTask task = new AssigmentTask(clusters, documents, 0,
				documents.length, numChanges, minSize);
		ForkJoinPool pool = new ForkJoinPool();

		pool.execute(task);

		task.join();
		pool.shutdown();

		System.out.println("Number of Changes: "+numChanges);

		if (numChanges.get()>0) {
			change=true;
		}
		
		return change;
	}


	private static void update(DocumentCluster[] clusters, int minSize) {
		UpdateTask task=new UpdateTask(clusters, 0, clusters.length,minSize);
		
		ForkJoinPool pool = new ForkJoinPool();

		pool.execute(task);

		task.join();
		pool.shutdown();
		

	}

}
