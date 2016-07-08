package com.javferna.packtpub.book.mastering.textcategorization.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Vocabulary {
	
	private ConcurrentHashMap<String,Word> voc;
	private AtomicInteger index;
	
	public Vocabulary() {
		voc=new ConcurrentHashMap<String, Word>();
		index=new AtomicInteger(0);
	}
	
	public synchronized void addWord(Item item) {
		Word word=voc.get(item.getItem());
		
		if (word==null) {
			word=new Word();
			word.setWord(item.getItem());
			word.setIndex(index.get());
			index.incrementAndGet();
			voc.put(item.getItem(), word);
		}
		word.addTf(item.getTfxidf());
		word.addDf(1);
	}

	public Word getWord(String key) {
		return voc.get(key);
	}

	public ConcurrentHashMap<String,Word> getVocabulary() {
		return voc;
		
	}

}
