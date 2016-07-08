package com.javferna.packtpub.book.mastering.textcategorization.common;

public class Attribute implements Comparable<Attribute> {

	private int index;
	private double value;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int compareTo(Attribute o) {
		if (this.getIndex() < o.getIndex()) {
			return -1;
		} else if (this.getIndex() > o.getIndex()){
			return 1;
		}
		return 0;
	}
	
	
}
