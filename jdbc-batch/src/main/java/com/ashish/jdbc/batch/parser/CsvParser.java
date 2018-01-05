package com.ashish.jdbc.batch.parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ashish.jdbc.batch.domain.BatchFilepojo;
import com.opencsv.CSVReader;

@Service
public class CsvParser {

	public List<BatchFilepojo> parse(String file) {
		List<BatchFilepojo> batchFilepojos = new ArrayList<BatchFilepojo>();
		 CSVReader reader = null;
	        try {
	            reader = new CSVReader(new FileReader(file), ',');
	            
	            String[] line;
	            reader.readNext();
	            while ((line = reader.readNext()) != null) {
	            	batchFilepojos.add(new BatchFilepojo(Integer.parseInt(line[0]), line[1], line[2], line[3], line[4], line[5], line[6], line[7]));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return batchFilepojos;
	}
}
