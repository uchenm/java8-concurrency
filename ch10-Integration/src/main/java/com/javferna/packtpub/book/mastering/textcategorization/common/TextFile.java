package com.javferna.packtpub.book.mastering.textcategorization.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextFile {

	private String fileName;
	private List<String> content;

	public TextFile(String fileName, List<String> content) {
		this.fileName = fileName;
		this.content = content;
	}

	public TextFile(Path path) throws IOException {
		this(path.getFileName().toString(), Files.readAllLines(path));
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getContent() {
		return content;
	}
	
	
}
