package com.javferna.packtpub.book.mastering.textcategorization.common;

public class Word {

	private String word;
	private int tf;
	private int df;
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public void addTf(double tfxidf) {
		this.tf+=tfxidf;
	}

	public void addDf(int df) {
		this.df+=df;
	}
	
	
}
