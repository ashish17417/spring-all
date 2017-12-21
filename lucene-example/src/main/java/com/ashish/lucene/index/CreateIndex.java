package com.ashish.lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex {

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";
	public static final boolean RECREATE_INDEX_IF_EXISTS = true;
	public AtomicInteger atomicInteger = new AtomicInteger(0);

	public IndexState createIndex(String indexDirectory, String filesToIndexDirectory) {
		Analyzer analyzer = new StandardAnalyzer();
		IndexState indexState = new IndexState();

		try {
			IndexWriter indexWriter = new IndexWriter(indexDirectory, analyzer, RECREATE_INDEX_IF_EXISTS);
			System.out.println("Please wait while indexing..");
			@SuppressWarnings("resource")
			Stream<Path> totalFiles = Files.walk(Paths.get(new URI(filesToIndexDirectory)));
			indexState.setTotalCountToBe((int) totalFiles.count());

			totalFiles.forEach(filePath -> {
				try {
					addFilesToIndex(filePath, indexWriter);
				} catch (IOException e) {
					System.out.println("Error while indexing file - " + filePath);
					e.printStackTrace();
				}
			});
			indexState.setTotalDone(atomicInteger.get());
			indexWriter.optimize();
			indexWriter.close();
		} catch (Exception e) {
			System.out.println("Indexing not completed..");
			e.printStackTrace();
			indexState.setStatus("FAIL");
			return indexState;
		}
		indexState.setStatus("SUCCESS");
		return indexState;
	}

	private void addFilesToIndex(Path filePath, IndexWriter indexWriter) throws IOException {
		Document document = null;
		Reader reader = null;
		document = new Document();
		document.add(
				new Field(FIELD_PATH, filePath.toFile().getCanonicalPath(), Field.Store.YES, Field.Index.UN_TOKENIZED));
		reader = new FileReader(filePath.toFile());
		document.add(new Field(FIELD_CONTENTS, reader));
		indexWriter.addDocument(document);
		atomicInteger.incrementAndGet();
	}

	public List<String> searchIndex(String searchString, String INDEX_DIRECTORY, Operator o) throws IOException,
			ParseException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		List<String> docSearchDetails = new ArrayList<String>();
		Directory directory = FSDirectory.getDirectory(INDEX_DIRECTORY);
		IndexReader indexReader = IndexReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
		// queryParser.setDefaultOperator(QueryParser.Operator.AND);
		queryParser.setDefaultOperator(o);
		Query query = queryParser.parse(searchString);
		String[] words = searchString.split(" ");
		TopDocs hits = indexSearcher.search(query, 1000);
		Map<String, Double> docSearchDetailsMap = new HashMap<String, Double>();
		int did = 1;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			String docc = doc.get("path");
			System.out.println("QueryId Q0 "
					+ (docc.substring(docc.lastIndexOf("\\") + 1) + " " + did + " " + scoreDoc.score + " STANDARD"));
			// System.out.println(" Score : "+scoreDoc.score);
			did++;
			docSearchDetailsMap.put(doc.get("path"), (double) scoreDoc.score);
			docSearchDetails.add(doc.get("path"));
			if (did > 200) {
				break;
			}
		}

		// System.out.println("doc result size " + docSearchDetails.size());
		MapComparator comparator = new MapComparator(docSearchDetailsMap);

		Map<String, Double> newMap = new TreeMap<String, Double>(comparator);
		newMap.putAll(docSearchDetailsMap);

		for (String key : docSearchDetailsMap.keySet()) {
			// docSearchDetails.add(key);
		}

		return docSearchDetails;
	}

	public static ArrayList<File> getAllFiles(String directoryName, ArrayList<File> files) {

		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				getAllFiles(file.getAbsolutePath(), files);
			}
		}

		return files;
	}

	public static void main(String args[]) throws IOException, ParseException, SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		String searchstr = "dog cow";
		CreateIndex index = new CreateIndex();
		index.createIndex("/indexed", "E:\\DownloadedExamples\\spring-batch-multithreading-example");
		Operator o = Operator.AND;
		System.out.println(index.searchIndex("Class", "/indexed", o));
	}

	private class IndexState {
		private String status;
		private int totalCountToBe;
		private int totalDone;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public int getTotalCountToBe() {
			return totalCountToBe;
		}

		public void setTotalCountToBe(int totalCountToBe) {
			this.totalCountToBe = totalCountToBe;
		}

		public int getTotalDone() {
			return totalDone;
		}

		public void setTotalDone(int totalDone) {
			this.totalDone = totalDone;
		}

	}

}
