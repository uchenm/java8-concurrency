package com.javferna.packtpub.book.mastering.textcategorization.common;

public class Item {

	private String item;
	private double tfxidf;
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public double getTfxidf() {
		return tfxidf;
	}
	
	public void setTfxidf(double tfxidf) {
		this.tfxidf = tfxidf;
	}

	public void addTf() {
		this.tfxidf++;
	}
}
