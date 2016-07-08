package com.javferna.packtpub.mastering.keywordExtraction.common;

public class Keyword implements Comparable<Keyword> {

	private String word;
	private int df;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	@Override
	public int compareTo(Keyword o) {
		return Integer.compare(o.getDf(), this.getDf());
	}

}
