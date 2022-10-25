package com.fabrikam;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;

import java.io.IOException;

import java.util.Arrays;

public class Indexer {
	public static void index(String method) {
		System.out.println(method);
		try {
			Path docFilePath = Paths.get("cran/cran.all.1400");
			Path indexDirPath = Paths.get("index/" + method);

			Analyzer analyzer = new EnglishAnalyzer();

			Directory indexDir = FSDirectory.open(indexDirPath);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

			if (method.equals("BM25")) {
				config.setSimilarity(new BM25Similarity());
				System.out.println("Similarity method has been set");
			}

			indexDir.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
