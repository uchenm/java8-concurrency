package com.javferna.packtpub.book.mastering.textcategorization.clustering;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.javferna.packtpub.book.mastering.textcategorization.common.Attribute;
import com.javferna.packtpub.book.mastering.textcategorization.common.Document;

public class DocumentCluster {

	private double centroid[];

	private ConcurrentLinkedDeque<Document> documents;

	public DocumentCluster(int size) {
		documents = new ConcurrentLinkedDeque<Document>();
		centroid = new double[size];
	}

	public void addDocument(Document document) {
		documents.offer(document);
	}

	public void clearDocuments() {
		documents.clear();
	}

	public void calculateCentroid() {

		for (int i = 0; i < centroid.length; i++) {
			centroid[i] = 0;
		}

		for (Document document : documents) {
			List<Attribute> vector = document.getExample();
			for (Attribute word : vector) {
					centroid[word.getIndex()] += word.getValue();
			}
		}

		for (int i = 0; i < centroid.length; i++) {
			centroid[i] = centroid[i] / documents.size();
		}
	}

	public double[] getCentroid() {
		return centroid;
	}

	public ConcurrentLinkedDeque<Document> getDocuments() {
		return documents;
	}

	public void initialize(Random random) {
		for (int i = 0; i < centroid.length; i++) {
			centroid[i] = random.nextDouble();
		}
	}

}
