package com.fabrikam;

import org.apache.lucene.queryparser.classic.ParseException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
		test("BM25");
    }

	public static void test(String method) {
		System.out.println("Testing " + method + " method...");

		Indexer indexer = new Indexer();
		indexer.index(method);
	}
}
