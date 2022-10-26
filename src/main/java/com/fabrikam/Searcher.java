package com.fabrikam;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.DirectoryReader;

import org.apache.lucene.search.IndexSearcher;

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
		isearcher = new IndexSearcher(DirectoryReader.open(this.directory));

		switch (this.algorithm) {
			case Classic:
				break;
			case BM25:
				break;
		}
	}
}
