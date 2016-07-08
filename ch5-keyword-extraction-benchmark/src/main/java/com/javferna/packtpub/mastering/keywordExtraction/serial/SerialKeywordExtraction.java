package com.javferna.packtpub.mastering.keywordExtraction.serial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.javferna.packtpub.mastering.keywordExtraction.common.Document;
import com.javferna.packtpub.mastering.keywordExtraction.common.DocumentParser;
import com.javferna.packtpub.mastering.keywordExtraction.common.Keyword;
import com.javferna.packtpub.mastering.keywordExtraction.common.Word;

public class SerialKeywordExtraction {

	public static void main(String[] args) {

		Date start, end;

		File source = new File("data");
		File[] files = source.listFiles();
		HashMap<String, Word> globalVoc = new HashMap<>();
		HashMap<String, Integer> globalKeywords = new HashMap<>();
		int totalCalls = 0;
		int numDocuments = 0;

		start = new Date();

		// Phase 1: Parse all the documents
		if (files == null) {
			System.err.println("Unable to read the 'data' folder");
			return;
		}

		for (File file : files) {

			if (file.getName().endsWith(".txt")) {
				Document doc = DocumentParser.parse(file.getAbsolutePath());
				for (Word word : doc.getVoc().values()) {
					globalVoc.merge(word.getWord(), word, Word::merge);
				}
				numDocuments++;
			}
		}
		System.out.println("Corpus: " + numDocuments + " documents.");

		// Phase 2: Update the df of the voc of the Documents
		for (File file : files) {
			if (file.getName().endsWith(".txt")) {
				Document doc = DocumentParser.parse(file.getAbsolutePath());
				List<Word> keywords = new ArrayList<>(doc.getVoc().values());
				for (Word word : keywords) {
					Word globalWord = globalVoc.get(word.getWord());
					word.setDf(globalWord.getDf(), numDocuments);
				}
				Collections.sort(keywords);

				if (keywords.size() > 10) {
					keywords = keywords.subList(0, 10);
				}
				
				for (Word word : keywords) {
					addKeyword(globalKeywords, word.getWord());
					totalCalls++;
				}
			}
		}

		// Phase 3: Get a list of a better keywords
		List<Keyword> orderedGlobalKeywords = new ArrayList<>();
		for (Entry<String, Integer> entry : globalKeywords.entrySet()) {
			Keyword keyword = new Keyword();
			keyword.setWord(entry.getKey());
			keyword.setDf(entry.getValue());
			orderedGlobalKeywords.add(keyword);
		}

		Collections.sort(orderedGlobalKeywords);

		if (orderedGlobalKeywords.size() > 100) {
			orderedGlobalKeywords = orderedGlobalKeywords.subList(0, 100);
		}

		for (Keyword keyword : orderedGlobalKeywords) {
			System.out.println(keyword.getWord() + ": " + keyword.getDf());
		}

		end = new Date();
		System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
		System.out.println("Vocabulary Size: " + globalVoc.size());
		System.out.println("Keyword Size: " + globalKeywords.size());
		System.out.println("Number of Documents: " + numDocuments);
		System.out.println("Total calls: " + totalCalls);

	}

	private static void addKeyword(HashMap<String, Integer> globalKeywords, String word) {
		globalKeywords.merge(word, 1, Integer::sum);
	}

}
