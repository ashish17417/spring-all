package com.ingleash.multithreaded.dbupdate;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.ingleash.multithreaded.dbupdate.batch.DbItemWriter;
import com.ingleash.multithreaded.dbupdate.batch.FileItemReader;
import com.ingleash.multithreaded.dbupdate.batch.JobCompletionListener;
import com.ingleash.multithreaded.dbupdate.domain.BatchFilepojo;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	JobRepository jobRepository;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	FileItemReader fileItemReader;
	
	@Autowired
	DbItemWriter dbItemWriter;
	
	@Bean
	public Job processJob() {
		return jobBuilderFactory.get("processJob")
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(orderStep1()).end().build();
	}

	@Bean
	public Step orderStep1() {
		return stepBuilderFactory.get("orderStep1").<BatchFilepojo, BatchFilepojo> chunk(1)
				.reader(fileItemReader)
				.writer(dbItemWriter).build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}
	
	@Bean
	public JobLauncher jobLauncher() {
	    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
	    jobLauncher.setJobRepository(jobRepository); 
	    jobLauncher.setTaskExecutor(taskExecutor());
	    return jobLauncher;
	}
	
	@Bean
	public TaskExecutor taskExecutor(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("spring_batch");
	    asyncTaskExecutor.setConcurrencyLimit(120);
	    return asyncTaskExecutor;
	}
}
