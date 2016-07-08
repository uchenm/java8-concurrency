package com.javferna.packtpub.book.mastering.textcategorization.common;

import java.util.HashMap;
import java.util.List;

import com.javferna.packtpub.book.mastering.textcategorization.clustering.DocumentCluster;

public class Document {

	private String name;
	private HashMap<String,Item> voc;
	private List<Attribute> example;
	private DocumentCluster cluster;
	
	public DocumentCluster getCluster() {
		return cluster;
	}

	public boolean setCluster(DocumentCluster cluster) {
		if (this.cluster==cluster) {
			return false;
		} else {
			this.cluster = cluster;
			return true;
		}
	}

	public List<Attribute> getExample() {
		return example;
	}

	public void setExample(List<Attribute> example) {
		this.example = example;
	}

	public Document() {
		voc=new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Item> getVoc() {
		return voc;
	}

	public void setVoc(HashMap<String, Item> voc) {
		this.voc = voc;
	}

	public void addWord(String token) {

		Item item=voc.get(token);
		if (item==null) {
			item=new Item();
			item.setItem(token);
			voc.put(token,item);
		}
		item.addTf();
	}
	
	
}
