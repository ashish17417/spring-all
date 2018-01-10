package com.ingleash.multithreaded.dbupdate.batch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ingleash.multithreaded.dbupdate.domain.BatchFilepojo;
import com.ingleash.multithreaded.dbupdate.domain.TestTable;
import com.ingleash.multithreaded.dbupdate.repo.TestTableRepository;

@Component
public class DbItemWriter implements ItemWriter<BatchFilepojo> {

	@Autowired
	TestTableRepository repository;
	
	public void write(List<? extends BatchFilepojo> items) throws Exception {
		Random rm = new Random();
		List<TestTable> list = items.stream().map(item-> {
			TestTable table = new TestTable(rm.nextInt(), 
					item.getMsd1(), 
					LocalDate.parse(item.getDob(), DateTimeFormatter.BASIC_ISO_DATE),
					item.getMsd2(),
					item.getMsd3(),
					item.getMsd4(),
					item.getMsd5(),
					item.getFlag().equals("0") ? false : true
					);
			return table;
				}
		).collect(Collectors.toList());
		
		repository.save(list);
	}

}
