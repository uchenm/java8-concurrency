package com.javferna.packtpub.mastering.keywordExtraction.common;

public class Word implements Comparable<Word> {

	private String word;
	private int tf;
	private int df;
	private double tfIdf;

	public Word(String word) {
		  this.word = word;
		  this.df = 1; 
		}
	
	public String getWord() {
		return word;
	}

	public int getTf() {
		return tf;
	}

	public void addTf() {
		  this.tf++;
	}
	
	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public void setDf(int df, int N) {
		this.df = df;
		tfIdf = tf * Math.log(Double.valueOf(N) / df);
	}

	public double getTfIdf() {
		return tfIdf;
	}

	@Override
	public int compareTo(Word o) {
		return Double.compare(o.getTfIdf(), this.getTfIdf());
	}
	
	public Word merge(Word other) {
		  if (this.word.equals(other.word)) { 
			  this.tf+=other.tf;
			  this.df+=other.df;
		  }
		  return this;
	}

}
