package com.javferna.packtpub.mastering.keywordExtraction.common;

import java.util.HashMap;

public class Document {
	
	private String fileName;
	
	private HashMap <String, Word> voc;
	
	public Document() {
		voc=new HashMap<>();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public HashMap<String, Word> getVoc() {
		return voc;
	}
	
	public void addWord(String string) {
		voc.computeIfAbsent(string, k -> new Word(k)).addTf();
	}

	@Override
	public String toString() {
		return fileName+": "+voc.size();
	}
	
	
	
	
}
