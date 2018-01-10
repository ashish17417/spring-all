package com.ashish.ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This class is the implementation of
 * Inverted Index and Boolean Retrieval Model
 * for information retrieval
 *
 */
public class InvertedIndexAndBMR {

	public static void main(String[] args) {

		String [] searchArray = 
		{"pune","town","city","university"};
		List<String> searchList = null;
		File file=null;
		File [] files=null;
		int fileCount=0;
		Map<String , Integer> fileMap=null;
		BufferedReader br = null;
		List<String> output1_1=new ArrayList<String>();;
		ArrayList<Integer> output1_2= new ArrayList<Integer>();;
		/*Final Output dataset*/
		Map<Map<String, Integer>, List<Integer>> output2 = 
			new HashMap<Map<String,Integer>, List<Integer>>();

		try {
			/*Creating searchList and sorting the same*/
			sortSearchList(searchArray);
			/*Loading documents and searching the search array*/
			file= new File("E:\\GIT_17417\\spring-all\\app-starter\\docs\\ir_docs");
			files=file.listFiles();
			fileMap = new HashMap<String, Integer>();
			fileCount=1;
			for(File file2 : files){
				fileMap.put(file2.getName(), fileCount);
				fileCount++;
			}
			/*Search the terms in the documents set*/
			searchTerms(searchArray, files, fileMap, output1_1, output1_2);
			/*Interim output with the term and document id where it is found*/
			interimOutput(output1_1, output1_2);
			/*The results with doc frequency and posting lists*/
			finalOutput(output1_1, output1_2, output2);
			/*Search results with boolean retrival model*/
			bmrOutput(fileMap, output2);


		} catch (FileNotFoundException e) {
			System.out.println("ERROR!!! "+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR!!! "+ e.getMessage());
			e.printStackTrace();

		}
	}

	private static void searchTerms(String[] searchArray, File[] files,
			Map<String, Integer> fileMap, List<String> output1_1,
			ArrayList<Integer> output1_2) throws FileNotFoundException,
			IOException {
		BufferedReader br;
		for(File file2 : files){
			br = new BufferedReader(new FileReader(file2));
			String line;
			while (( line = br.readLine()) != null) {
				for(String find : searchArray){
					if(line.toUpperCase().
							contains(find.toUpperCase())){
						output1_1.add(find);
						output1_2.add(fileMap.get
								(file2.getName()));
					}
				}
			}
		}
	}

	private static void bmrOutput(Map<String, Integer> fileMap,
			Map<Map<String, Integer>, List<Integer>> output2) {
		System.out.println("------------------Boolean Retrival Model Output----------------- ");
		/*Now implementing boolean retrival model*/
		/*Query to be passed is pune AND university*/
		String query ="pune AND university";
		List<List<Integer>> bmrResult=new ArrayList<List<Integer>>();
		for(Map<String , Integer> map2 : output2.keySet()){
			for(String keys: map2.keySet()){
				for(String str : query.split(" AND ")){
					if(keys.equalsIgnoreCase(str)){
						//bmrResult.add(arg0)bmrResult.a
						bmrResult.add(output2.get(map2));
					}
				}
			}
		}
		bmrResult.get(0).retainAll(bmrResult.get(1));
		for(Entry<String, Integer> into : fileMap.entrySet()){
			if(into.getValue().equals(bmrResult.get(0).get(0))){
				System.out.println("After Boolean Retrival Model document" +
						" for search query - " +bmrResult.get(0).get(0) + " i.e. "+into.getKey());
			}
		}
	}

	private static void finalOutput(List<String> output1_1,
			ArrayList<Integer> output1_2,
			Map<Map<String, Integer>, List<Integer>> output2) {
		Map<String , Integer> map= new TreeMap<String, Integer>(); 
		List<Integer> postingLists= new ArrayList<Integer>();
		/*Setting up doc frequency*/
		for(int i=0;i<output1_1.size();i++){
			int count=1;
			if(map.keySet().contains(output1_1.get(i))){
				count=map.get(output1_1.get(i));
				count=count+1;
			}
			map.put(output1_1.get(i), count);
		}
		/*Setting up posting Lists*/
		for(String keys: map.keySet()){
			Map<String , Integer> map2= new TreeMap<String, Integer>(); 
			map2.put(keys, map.get(keys));
			output2.put(map2, null);
		}
		for(Map<String , Integer> map2 : output2.keySet()){
			postingLists= new ArrayList<Integer>();
			for(String keys: map2.keySet()){
				for(int i=0;i<output1_1.size();i++){
					if(output1_1.get(i).equalsIgnoreCase(keys)){
						postingLists.add(output1_2.get(i));
					}
				}
			}
			output2.put(map2, postingLists);
		}
		System.out.println("------------------final Output----------------- ");
		for(Map<String , Integer> map2 : output2.keySet()){
			for(String keys: map2.keySet()){
				System.out.print("For term - " +keys +" Document Frequency is " + map2.get(keys) +" and it is found in docid - " );
				for(Integer listings : output2.get(map2)){
					System.out.print("  " + listings +" ");
				}
				System.out.println();
			}
		}
	}

	private static void interimOutput(List<String> output1_1,
			ArrayList<Integer> output1_2) {
		System.out.println("------------------interim Output----------------- ");
		System.out.println("Term   -   DocId");
		for(int i=0;i<output1_1.size();i++){
			System.out.println
			(output1_1.get(i) +"  -  " + output1_2.get(i));
		}
	}

	private static void sortSearchList(String[] searchArray) {
		List<String> searchList;
		searchList = Arrays.asList(searchArray);
		searchList = new ArrayList<String>(searchList);
		Collections.sort(searchList);
	}
}
