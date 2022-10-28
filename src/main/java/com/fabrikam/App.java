package com.fabrikam;

import org.apache.lucene.queryparser.classic.ParseException;
import java.util.Arrays;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
		test("BM25");
    }

	public static void test(String method) throws IOException {
		System.out.println("Testing " + method + " method...");

		Indexer indexer = new Indexer("cran/cran.all.1400", "index/" + method);
		indexer.index(method);

		Searcher searcher = new Searcher("index/" + method, ScoreAlgos.BM25);
		searcher.createSearcher();

		searcher.runQrys("cran/cran.qry", "results", "BM25.txt", 30);
	}
}
