package com.fabrikam;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

import java.util.Arrays;

public class Indexer {
	public static void index(String method) {
		//System.out.println(method);
		try {
			Path docFilePath = Paths.get("cran/cran.all.1400");
			Path indexDirPath = Paths.get("index/" + method);

			Directory indexDir = FSDirectory.open(indexDirPath);
		
			System.out.println(Arrays.toString(indexDir.listAll()));

			indexDir.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
