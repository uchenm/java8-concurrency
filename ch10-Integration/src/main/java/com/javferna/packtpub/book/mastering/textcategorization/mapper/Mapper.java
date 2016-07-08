package com.javferna.packtpub.book.mastering.textcategorization.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.javferna.packtpub.book.mastering.textcategorization.common.Attribute;
import com.javferna.packtpub.book.mastering.textcategorization.common.Document;
import com.javferna.packtpub.book.mastering.textcategorization.common.Vocabulary;
import com.javferna.packtpub.book.mastering.textcategorization.common.Word;

public class Mapper implements Runnable {
	
	private ConcurrentLinkedDeque<Document> documents;
	private Vocabulary voc;

	public Mapper (ConcurrentLinkedDeque<Document> documents, Vocabulary voc) {
		this.documents=documents;
		this.voc=voc;
	}
	

	@Override
	public void run() {
		Document doc;
		int counter=0;
		System.out.println(Thread.currentThread().getName()+": Mapper start");
		while ((doc=documents.pollFirst())!=null) {
			counter++;
			List<Attribute> attributes=new ArrayList<>();
			doc.getVoc().forEach((key, item)-> {
				Word word=voc.getWord(key);
				item.setTfxidf(item.getTfxidf()/word.getDf());
				
				Attribute attribute=new Attribute();
				attribute.setIndex(word.getIndex());
				attribute.setValue(item.getTfxidf());
				attributes.add(attribute);
			});
			
			Collections.sort(attributes);
			doc.setExample(attributes);
		}
		System.out.println(Thread.currentThread().getName()+": Mapper end: "+counter);
		
	}

}
