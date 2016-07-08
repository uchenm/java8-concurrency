/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.javferna.packtpub.mastering.keywordExtraction.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;

import com.javferna.packtpub.mastering.keywordExtraction.common.Document;
import com.javferna.packtpub.mastering.keywordExtraction.common.DocumentParser;
import com.javferna.packtpub.mastering.keywordExtraction.common.Keyword;
import com.javferna.packtpub.mastering.keywordExtraction.common.Word;
import com.javferna.packtpub.mastering.keywordExtraction.concurrent.KeywordExtractionTask;

@State(Scope.Benchmark)
public class MyBenchmark {
	
	@Param({"1","2","3"})
	private int factor;

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10, batchSize = 1)
	@Measurement(iterations = 10, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Timeout(time=1, timeUnit=TimeUnit.DAYS)
    public void serialKeywordExtraction() {
		File source = new File("data");
		File[] files = source.listFiles();
		HashMap<String, Word> globalVoc = new HashMap<>();
		HashMap<String, Integer> globalKeywords = new HashMap<>();
		int totalCalls = 0;
		int numDocuments = 0;

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

				if(keywords.size() > 10) {
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
		System.out.println("Vocabulary Size: " + globalVoc.size());
		System.out.println("Keyword Size: " + globalKeywords.size());
		System.out.println("Number of Documents: " + numDocuments);
		System.out.println("Total calls: " + totalCalls);   
	}
    
	private static void addKeyword(HashMap<String, Integer> globalKeywords, String word) {
		globalKeywords.merge(word, 1, Integer::sum);
	}
	
	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@Fork(1)
	@Warmup(iterations = 10,  batchSize = 1)
	@Measurement(iterations = 10, batchSize = 1)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Timeout(time=1, timeUnit=TimeUnit.DAYS)
    public void concurrentKeywordExtraction() {

		ConcurrentHashMap<String, Word> globalVoc = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Integer> globalKeywords = new ConcurrentHashMap<>();

		File source = new File("data");

		File[] files = source.listFiles(f -> f.getName().endsWith(".txt"));
		if (files == null) {
			System.err.println("The 'data' folder not found!");
			return;
		}
		ConcurrentLinkedDeque<File> concurrentFileListPhase1 = new ConcurrentLinkedDeque<>(Arrays.asList(files));
		ConcurrentLinkedDeque<File> concurrentFileListPhase2 = new ConcurrentLinkedDeque<>(Arrays.asList(files));

		int numDocuments = files.length;

		int numTasks = factor * Runtime.getRuntime().availableProcessors();
		System.out.println(numTasks);
		Phaser phaser = new Phaser();

		Thread threads[] = new Thread[numTasks];
		KeywordExtractionTask tasks[] = new KeywordExtractionTask[numTasks];

		
		for (int i = 0; i < numTasks; i++) {
			tasks[i] = new KeywordExtractionTask(concurrentFileListPhase1, concurrentFileListPhase2, phaser, globalVoc,
					globalKeywords, concurrentFileListPhase1.size(), "Task " + i, i==0);
			phaser.register();
			System.out.println(phaser.getRegisteredParties() + " tasks arrived to the Phaser.");
		}

		for (int i = 0; i < numTasks; i++) {
			threads[i] = new Thread(tasks[i]);
			threads[i].start();
		}

		for (int i = 0; i < numTasks; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Is Terminated: " + phaser.isTerminated());

		System.out.println("Vocabulary Size: " + globalVoc.size());
		System.out.println("Number of Documents: " + numDocuments);
		
	}

}
