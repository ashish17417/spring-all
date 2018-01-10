package com.ashish.lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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

/**
 * @author ashish Ingle (720171)
 * built with Java 8
 */
public class CreateIndex {

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";
	public static final boolean RECREATE_INDEX_IF_EXISTS = true;
	public AtomicInteger atomicInteger = new AtomicInteger(0);

	/**
	 * @param indexDirectory : Directoy where indexes to be stored
	 * @param filesToIndexDirectory : The files for which indexes to be created
	 * @return {@link IndexState} : Gives status of the indexing
	 */
	public IndexState createIndex(String indexDirectory, String filesToIndexDirectory) {
		Analyzer analyzer = new StandardAnalyzer();
		IndexState indexState = new IndexState();

		try {
			IndexWriter indexWriter = new IndexWriter(indexDirectory, analyzer, RECREATE_INDEX_IF_EXISTS);
			System.out.println("Please wait while indexing..");
			@SuppressWarnings("resource")
			Stream<Path> totalFiles = Files.walk(Paths.get(new File(filesToIndexDirectory).toURI()));
			indexState.setTotalCountToBe((int) totalFiles.count());
			Files.walk(Paths.get(new File(filesToIndexDirectory).toURI())).forEach(filePath -> {
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

	/**
	 * @param searchString - String to be searched
	 * @param indexStore - Index stores
	 * @param operator {@link Operator} Type of operator.IN case of multiple words
	 *        Operator like AND or OR can be used.
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<String> searchIndex(String searchString, String indexStore, Operator operator) throws IOException,
			ParseException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		List<String> docSearchDetails = new ArrayList<String>();
		Directory directory = FSDirectory.getDirectory(indexStore);
		IndexReader indexReader = IndexReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
		queryParser.setDefaultOperator(operator);
		Query query = queryParser.parse(searchString);
		String[] words = searchString.split(" ");
		TopDocs hits = indexSearcher.search(query, 1000);
		Map<String, Double> docSearchDetailsMap = new HashMap<String, Double>();
		int did = 1;
		
		//The score of this document for the query
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			String docc = doc.get("path");
			System.out.println("QueryId Q "+did + " "
					+ (docc.substring(docc.lastIndexOf("\\") + 1) + " " + did + " " + scoreDoc.score + " STANDARD"));
			did++;
			docSearchDetailsMap.put(doc.get("path"), (double) scoreDoc.score);
			docSearchDetails.add(doc.get("path"));
			if (did > 200) {
				break;
			}
		}

		MapComparator comparator = new MapComparator(docSearchDetailsMap);
		Map<String, Double> newMap = new TreeMap<String, Double>(comparator);
		newMap.putAll(docSearchDetailsMap);
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

  class IndexState {
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
	
	private class MapComparator implements Comparator<Object> {

		Map<String, Double> map;

		public MapComparator(Map<String, Double> map) {
			this.map = map;
		}

		public int compare(Object o1, Object o2) {

			if (map.get(o2) == map.get(o1))
				return 1;
			else
				return ((Double) map.get(o2)).compareTo((Double)     
						map.get(o1));

		}

}
}
