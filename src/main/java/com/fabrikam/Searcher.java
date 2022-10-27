package com.fabrikam;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.IOException;

import java.util.ArrayList;

public class Searcher {
	private Directory directory;
	private ScoreAlgos algorithm;
	private IndexSearcher isearcher;
	private Analyzer analyzer;

	public Searcher(String dirPath, ScoreAlgos algorithm) throws IOException {
		this.directory = FSDirectory.open(Paths.get(dirPath));
		this.algorithm = algorithm;
		this.analyzer = new EnglishAnalyzer();
	}

	public void createSearcher() throws IOException {
		this.isearcher = new IndexSearcher(DirectoryReader.open(this.directory));

		switch (this.algorithm) {
			case Classic:
				this.isearcher.setSimilarity(new ClassicSimilarity());
				break;
			case BM25:
				this.isearcher.setSimilarity(new BM25Similarity());
				break;
		}
	}

	// TODO: Complete below function
	public ScoreDoc[] query(String query) throws IOException {
		TokenStream ts = this.analyzer.tokenStream(DocTags.TEXT.name, query);
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		ts.reset();

		ArrayList<String> terms = new ArrayList<>();

		while (ts.incrementToken()) {
			System.out.println(termAtt.toString());
			terms.add(termAtt.toString());
		}

		ts.close();
		return null;
	}

	public void runQrys(String qryFilePath) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(qryFilePath)));
		String[] items = content.split(DocTags.ID.tag + " (?=[0-9]+[\n\r]+)");

		for (String item : items) {
			item = item.trim();

			if (!item.isEmpty()) {
				ScoreDoc[] res = query(item.split(DocTags.TEXT.tag + "([\r\n]|\r\n)")[1]);
				break; // to run just one query for testing
			}
		}
	}
}
