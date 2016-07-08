package com.javferna.packtpub.book.mastering.textcategorization.clustering;

import java.util.concurrent.RecursiveAction;

public class UpdateTask extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1005287726225369344L;

	private DocumentCluster clusters[];

	private int start, end;

	private int minSize;

	public UpdateTask(DocumentCluster clusters[], int start, int end,
			int minSize) {
		this.clusters = clusters;
		this.start = start;
		this.end = end;
		this.minSize = minSize;
	}

	@Override
	protected void compute() {
		if (end - start < minSize) {
			for (int i = start; i < end; i++) {
				DocumentCluster cluster = clusters[i];
				cluster.calculateCentroid();
			}
		} else {
			int mid = (start + end) / 2;
			UpdateTask task1 = new UpdateTask(clusters, start, mid, minSize);
			UpdateTask task2 = new UpdateTask(clusters, mid, end, minSize);

			invokeAll(task1, task2);
		}

	}

}
