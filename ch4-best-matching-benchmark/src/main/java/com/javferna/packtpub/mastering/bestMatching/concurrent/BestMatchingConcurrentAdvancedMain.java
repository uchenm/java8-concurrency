package com.javferna.packtpub.mastering.bestMatching.concurrent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.javferna.packtpub.mastering.bestMatching.common.BestMatchingData;
import com.javferna.packtpub.mastering.bestMatching.data.WordsLoader;

public class BestMatchingConcurrentAdvancedMain {

	public static void main(String[] args) {
		try {
			Date startTime, endTime;
			List<String> dictionary=WordsLoader.load("data/UK Advanced Cryptics Dictionary.txt");
			
			System.out.println("Dictionary Size: "+dictionary.size());
			
			startTime=new Date();
			BestMatchingData result;
			result = BestMatchingAdvancedConcurrentCalculation.getBestMatchingWords(args[0], dictionary);
			List<String> results=result.getWords();
			endTime=new Date();
			System.out.println("Word: "+args[0]);
			System.out.println("Minimun distance: "+result.getDistance());
			System.out.println("List of best matching words: "+results.size());
			for (String word: results) {
				System.out.println(word);
			}
			System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

}
