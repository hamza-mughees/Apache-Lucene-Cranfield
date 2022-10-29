package com.fabrikam;

import org.apache.lucene.queryparser.classic.ParseException;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;

public class App {
	private static String CRAN_DOCS_PATH = "cran/cran.all.1400";
	private static String INDEX_DIR_PATH = "index/";
	private static String CRAN_QRYS_PATH = "cran/cran.qry";
	private static String RES_DIR_PATH = "results";

    public static void main(String[] args) throws IOException {
		searchEngine(Analyzers.STANDARD, ScoreAlgos.CLASSIC);
		searchEngine(Analyzers.STANDARD, ScoreAlgos.BM25);
    }

	public static void searchEngine(Analyzers analyzer, ScoreAlgos algorithm) throws IOException {
		Indexer indexer = new Indexer(CRAN_DOCS_PATH, INDEX_DIR_PATH, analyzer);
        indexer.index();

		Searcher searcher = new Searcher(INDEX_DIR_PATH, algorithm, indexer.getAnalyzer(), indexer.getAnalyzerDir());
		searcher.runQrys(CRAN_QRYS_PATH, RES_DIR_PATH, 30);
	}
}
