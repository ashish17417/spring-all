package com.ashish.jdbc.batch.controller;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashish.jdbc.batch.db.Persister;
import com.ashish.jdbc.batch.parser.CsvParser;

@RestController
public class BatchController {

	@Autowired
	private CsvParser csvParser;
	
	@Autowired
	private Persister persister;
	
	@GetMapping
	public String triggerBatch() throws SQLException {
		LocalDateTime start = LocalDateTime.now();
		persister.persist(csvParser.parse("output.txt"));
		LocalDateTime end = LocalDateTime.now();
		return  Duration.between(start, end).toString();
	}
	
	
}
