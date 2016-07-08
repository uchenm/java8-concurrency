package com.javferna.packtpub.mastering.bestMatching.serial;

import java.util.Date;
import java.util.List;

import com.javferna.packtpub.mastering.bestMatching.data.WordsLoader;

public class ExistSerialMain {

	public static void main(String[] args) {

		Date startTime, endTime;
		List<String> dictionary=WordsLoader.load("data/UK Advanced Cryptics Dictionary.txt");
		
		System.out.println("Dictionary Size: "+dictionary.size());
		
		startTime=new Date();
		boolean result=ExistSerialCalculation.existWord(args[0], dictionary);
		endTime=new Date();
		
		System.out.println("Word: "+args[0]);
		System.out.println("Exists: "+result);
		System.out.println("Execution Time: "+(endTime.getTime()-startTime.getTime()));
	}

}
