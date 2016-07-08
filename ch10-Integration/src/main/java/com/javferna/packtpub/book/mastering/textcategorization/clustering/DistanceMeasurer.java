package com.javferna.packtpub.book.mastering.textcategorization.clustering;

import java.util.List;

import com.javferna.packtpub.book.mastering.textcategorization.common.Attribute;

public class DistanceMeasurer {
	
	public static double euclideanDistance(Attribute[] words, double centroid[]) {
		double distance=0;
		
		int wordIndex=0;
		for (int i=0; i<centroid.length; i++) {
			if ((wordIndex<words.length)&&(words[wordIndex].getIndex()==i)) {
				distance+=Math.pow((words[wordIndex].getValue()-centroid[i]),2);
				wordIndex++;
			} else {
				distance+=centroid[i]*centroid[i];
			}
		}
		
		return Math.sqrt(distance);
	}
	
	public static double euclideanDistance(List<Attribute> words, double centroid[]) {
		double distance=0;
		
		int wordIndex=0;
		Attribute actualWord=words.get(wordIndex);
		
		for (int i=0; i<centroid.length; i++) {
			if ((wordIndex<words.size())&&(actualWord.getIndex()==i)) {
				distance+=Math.pow((actualWord.getValue()-centroid[i]),2);
				wordIndex++;
				if (wordIndex < words.size()) {
					actualWord=words.get(wordIndex);
				}
			} else {
				distance+=centroid[i]*centroid[i];
			}
		}
		
		return Math.sqrt(distance);
	}

}
