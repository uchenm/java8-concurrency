package com.javferna.packtpub.book.mastering.textcategorization.clustering;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

import com.javferna.packtpub.book.mastering.textcategorization.common.Document;

public class AssigmentTask extends RecursiveAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8709589012589400116L;

	private DocumentCluster clusters[];
	
	private Document documents[];
	
	private int start, end;
	
	private AtomicInteger numChanges;
	
	private int minSize;
	
	public AssigmentTask(DocumentCluster clusters[], Document documents[], int start, int end, AtomicInteger numChanges, int minSize) {
		this.clusters=clusters;
		this.documents=documents;
		this.start=start;
		this.end=end;
		this.numChanges=numChanges;
		this.minSize=minSize;
	}
	
	
	@Override
	protected void compute() {
		if (end-start<minSize) {
			for (int i=start; i<end; i++) {
				Document document=documents[i];
				double distance=Double.MAX_VALUE;
				DocumentCluster selectedCluster=null;
				for (DocumentCluster cluster : clusters) {
					double tmpDistance=0.0;
					tmpDistance=DistanceMeasurer.euclideanDistance(document.getExample(), cluster.getCentroid());
					if (tmpDistance < distance) {
						distance=tmpDistance;
						selectedCluster=cluster;
					}
				}
				selectedCluster.addDocument(document);
				
				boolean result=document.setCluster(selectedCluster);
				if (result) {
					numChanges.incrementAndGet();
				}

			}
		} else {
			int mid=(start+end)/2;
			AssigmentTask task1=new AssigmentTask(clusters, documents, start, mid, numChanges,minSize);
			AssigmentTask task2=new AssigmentTask(clusters, documents, mid, end, numChanges,minSize);
			
			invokeAll(task1,task2);
		}
	}


	public DocumentCluster[] getClusters() {
		return clusters;
	}


	public void setClusters(DocumentCluster[] clusters) {
		this.clusters = clusters;
	}


	public Document[] getDocuments() {
		return documents;
	}


	public void setDocuments(Document[] documents) {
		this.documents = documents;
	}

}
