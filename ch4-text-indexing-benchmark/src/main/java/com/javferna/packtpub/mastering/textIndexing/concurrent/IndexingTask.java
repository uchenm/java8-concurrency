package com.javferna.packtpub.mastering.textIndexing.concurrent;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

import com.javferna.packtpub.mastering.textIndexing.common.Document;
import com.javferna.packtpub.mastering.textIndexing.common.DocumentParser;

public class IndexingTask implements Callable<Document> {

	private File file;
	
	public IndexingTask(File file) {
		this.file=file;
	}
	@Override
	public Document call() throws Exception {
		DocumentParser parser = new DocumentParser();
	
		Map<String, Integer> voc = parser.parse(file.getAbsolutePath());
		
		Document document=new Document();
		document.setFileName(file.getName());
		document.setVoc(voc);
		return document;
	}

}
