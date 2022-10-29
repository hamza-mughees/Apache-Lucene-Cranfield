package com.fabrikam;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;

import java.nio.file.Paths;
import java.nio.file.Files;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

import java.util.ArrayList;

public class Searcher {
	private Directory directory;
	private ScoreAlgos algorithm;
	private IndexSearcher isearcher;
	private Analyzer analyzer;
	private String resFileName;
	private String analyzerDir;

	public Searcher(String dirPath, ScoreAlgos algorithm, Analyzer analyzer, String analyzerDir) throws IOException {
		this.analyzerDir = analyzerDir;
		this.directory = FSDirectory.open(Paths.get(dirPath + this.analyzerDir));
		this.algorithm = algorithm;
		this.analyzer = analyzer;
		this.isearcher = new IndexSearcher(DirectoryReader.open(this.directory));

		switch (this.algorithm) {
			case CLASSIC:
				this.isearcher.setSimilarity(new ClassicSimilarity());
				this.resFileName = "Classic";
				break;
			case BM25:
				this.isearcher.setSimilarity(new BM25Similarity());
				this.resFileName = "BM25";
				break;
		}
	}

	public ScoreDoc[] query(String query, int nTop) throws IOException {
		TokenStream ts = this.analyzer.tokenStream(DocTags.TEXT.name, query);
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		ts.reset();

		BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();

		while (ts.incrementToken()) {
//			System.out.println(termAtt.toString());
			String term = termAtt.toString();

			TermQuery tQry = new TermQuery(new Term(DocTags.TEXT.name, term));
			bqBuilder.add(tQry, BooleanClause.Occur.SHOULD);
		}

		ts.close();

		TopDocs topDocs = this.isearcher.search(bqBuilder.build(), nTop);
		return topDocs.scoreDocs;
	}

	public void runQrys(String qryFilePath, String resDirPath, int nTop) throws IOException {
		File resDir = new File(resDirPath);
		if (!resDir.exists())
			resDir.mkdir();

		File outputDir = new File(resDirPath + "/" + this.analyzerDir);
		if (!outputDir.exists())
			outputDir.mkdir();

		PrintWriter pwriter = new PrintWriter(resDirPath + "/" + this.analyzerDir + this.resFileName + ".txt", "UTF-8");

		String content = new String(Files.readAllBytes(Paths.get(qryFilePath)));
		String[] items = content.split(DocTags.ID.tag + " (?=[0-9]+[\n\r]+)");

		for (int qi = 0; qi < items.length; qi++) {
			String item = items[qi].trim();

			if (!item.isEmpty()) {
				ScoreDoc[] topHits = query(item.split(DocTags.TEXT.tag + "([\r\n]|\r\n)")[1], nTop);

				for (int di = 0; di < topHits.length; di++) {
					ScoreDoc topHit = topHits[di];
					Document doc = isearcher.doc(topHit.doc);

					pwriter.println(qi + " 0 " + doc.get(DocTags.ID.name) + " "
									+ (di+1) + " " + topHit.score + " " + this.resFileName);

//					System.out.println(doc.toString());
				}

//				break; // to run just one query for testing
			}
		}

		pwriter.close();
	}
}
