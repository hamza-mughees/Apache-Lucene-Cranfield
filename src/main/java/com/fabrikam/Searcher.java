package com.fabrikam;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.ScoreDoc;

import java.nio.file.Paths;

import java.io.IOException;

public class Searcher {
	private Directory directory;
	private ScoreAlgos algorithm;
	private IndexSearcher isearcher;

	public Searcher(String dirPath, ScoreAlgos algorithm) throws IOException {
		this.directory = FSDirectory.open(Paths.get(dirPath));
		this.algorithm = algorithm;
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
		return null;
	}
}
