package com.ingleash.multithreaded.dbupdate.batch;
import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

	@Autowired
	Job job;
	
	@Autowired
	JobLauncher jobLauncher;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/launchjob")
	public String handle() throws Exception {
		LocalDateTime start = LocalDateTime.now();
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		LocalDateTime end = LocalDateTime.now();
		logger.info("Completed in {} " ,Duration.between(start, end).getSeconds());
        
		
		return "Done Called in background";
	}
	
	
}