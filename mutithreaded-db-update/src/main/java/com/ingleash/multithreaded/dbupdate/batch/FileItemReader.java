package com.ingleash.multithreaded.dbupdate.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import com.ingleash.multithreaded.dbupdate.domain.BatchFilepojo;

@Component
public class FileItemReader extends FlatFileItemReader<BatchFilepojo> {

	public FileItemReader() {
		this.setResource(new FileSystemResource("output.txt"));
		this.setLinesToSkip(1);
		this.setLineMapper(new DefaultLineMapper<BatchFilepojo>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "id","msd1","dob","msd2","msd3","msd4","msd5","flag" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<BatchFilepojo>() {
					{
						setTargetType(BatchFilepojo.class);
					}
				});
			}
		});
	
	}
	
}
