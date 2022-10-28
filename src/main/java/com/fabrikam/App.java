package com.fabrikam;

import org.apache.lucene.queryparser.classic.ParseException;
import java.util.Arrays;

import java.io.IOException;

public class App {
	private static String CRAN_DOCS_PATH = "cran/cran.all.1400";
	private static String INDEX_DIR_PATH = "index/";
	private static String CRAN_QRYS_PATH = "cran/cran.qry";
	private static String RES_DIR_PATH = "results";

    public static void main(String[] args) throws IOException {
		index();

		search(ScoreAlgos.BM25);
		search(ScoreAlgos.Classic);
    }

	public static void index() throws IOException {
		Indexer indexer = new Indexer(CRAN_DOCS_PATH, INDEX_DIR_PATH);
		indexer.index();
	}

	public static void search(ScoreAlgos algorithm) throws IOException {
		Searcher searcher = new Searcher(INDEX_DIR_PATH, algorithm);
		searcher.runQrys(CRAN_QRYS_PATH, RES_DIR_PATH, 30);
	}
}
