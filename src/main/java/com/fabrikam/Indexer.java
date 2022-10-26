package com.fabrikam;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
				System.out.println("Similarity method set.");
			}

			IndexWriter iwriter = new IndexWriter(indexDir, config);

			/*
			try (InputStream is = Files.newInputStream(docFilePath)) {
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);
				System.out.println("Buffer reader created.");

				for (String l = br.readLine(); l != null; l = br.readLine()) {
					//System.out.println(l);

					Document doc = new Document();

					if (l.startsWith(DocTags.ID.tag)) {
						String sfValue = l.substring(3, l.length());
						doc.add(new StringField(DocTags.ID.name, sfValue, Field.Store.YES));

						//System.out.println(doc.get(DocTags.ID.name));
						iwriter.addDocument(doc);
					}
				}

			} finally {
				indexDir.close();
				iwriter.close();
			}
			*/

			FieldType vectorField = new FieldType(TextField.TYPE_STORED);
			vectorField.setTokenized(true);
			vectorField.setStoreTermVectors(true);
			vectorField.setStoreTermVectorPositions(true);
			vectorField.setStoreTermVectorOffsets(true);
			vectorField.setStoreTermVectorPayloads(true);

			ArrayList<Document> docs = new ArrayList<>();

			String content = new String(Files.readAllBytes(docFilePath));
			String[] items = content.split(DocTags.ID.tag + " (?=[0-9]+)");
			System.out.println(Arrays.toString(items));
			for (String item : items) {
				if (!item.isEmpty()) {
					docs.add(process(item, vectorField));
				}
			}

			iwriter.addDocuments(docs);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static Document process(String item, FieldType fieldType) {
		Document doc = new Document();
		String[] fields = item.split(".[TAWB]([\r\n]|\r\n)", -1);

		doc.add(new StringField(DocTags.ID.name, fields[0].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.TITLE.name, fields[1].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.AUTHOR.name, fields[2].trim(), Field.Store.YES));
		doc.add(new StringField(DocTags.BIBLIOGRAPHY.name, fields[3].trim(), Field.Store.YES));
		doc.add(new Field(DocTags.TEXT.name, fields[4].trim(), fieldType));

		return doc;
	}

}
