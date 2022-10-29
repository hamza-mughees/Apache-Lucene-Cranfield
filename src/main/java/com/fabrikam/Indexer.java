package com.fabrikam;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class Indexer {
	private Path docFilePath;
	private Directory indexDir;
	private Analyzer analyzer;
	private String analyzerDir;

	public Indexer(String docFilePath, String indexDirPath, Analyzers analyzer) throws IOException {
		this.docFilePath = Paths.get(docFilePath);

		switch (analyzer) {
			case STANDARD:
				this.analyzer = new StandardAnalyzer();
				this.analyzerDir = "standard/";
				this.indexDir = FSDirectory.open(Paths.get(indexDirPath + this.analyzerDir));
				break;
			case ENGLISH:
				this.analyzer = new EnglishAnalyzer();
				this.analyzerDir = "english/";
				this.indexDir = FSDirectory.open(Paths.get(indexDirPath + this.analyzerDir));
				break;
			case WHITESPACE:
				this.analyzer = new WhitespaceAnalyzer();
				this.analyzerDir = "whitespace/";
				this.indexDir = FSDirectory.open(Paths.get(indexDirPath + this.analyzerDir));
				break;
			case SIMPLE:
				this.analyzer = new SimpleAnalyzer();
				this.analyzerDir = "simple/";
				this.indexDir = FSDirectory.open(Paths.get(indexDirPath + this.analyzerDir));
				break;
		}

		System.out.println("Creating index using the " + this.analyzerDir.substring(0, this.analyzerDir.length()-1) + " analyzer.");
	}

	public void index() throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(this.analyzer);

		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		IndexWriter iwriter = new IndexWriter(this.indexDir, config);

		FieldType vectorField = new FieldType(TextField.TYPE_STORED);
		vectorField.setTokenized(true);
		vectorField.setStoreTermVectors(true);
		vectorField.setStoreTermVectorPositions(true);
		vectorField.setStoreTermVectorOffsets(true);
		vectorField.setStoreTermVectorPayloads(true);

		ArrayList<Document> docs = new ArrayList<>();

		String content = new String(Files.readAllBytes(this.docFilePath));
		String[] items = content.split(DocTags.ID.tag + " (?=[0-9]+)");

		for (String item : items) {
			if (!item.isEmpty()) {
				docs.add(generateFieldsDoc(item, vectorField));
			}
		}

		iwriter.addDocuments(docs);

		iwriter.close();
	}

	public Document generateFieldsDoc(String item, FieldType fieldType) {
		Document doc = new Document();
		String[] fields = item.split(".[TAWB]([\r\n]|\r\n)", -1);

		doc.add(new StringField(DocTags.ID.name, fields[0].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.TITLE.name, fields[1].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.AUTHOR.name, fields[2].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.BIBLIOGRAPHY.name, fields[3].trim(), Field.Store.YES));
		doc.add(new Field(DocTags.TEXT.name, fields[4].trim(), fieldType));

		return doc;
	}

	public String getAnalyzerDir() {
		return this.analyzerDir;
	}

	public Analyzer getAnalyzer() {
		return this.analyzer;
	}
}
