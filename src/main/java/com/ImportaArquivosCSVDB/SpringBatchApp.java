package com.ImportaArquivosCSVDB;
 
import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
 
@SpringBootApplication
@EnableScheduling
@Component
public class SpringBatchApp
{
    @Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
      
    public static void main(String[] args) {

        SpringApplication.run(SpringBatchApp.class, args);
    }

    /**
     * This Job Will Run Once the all the beans are created and job will load the database with given csv file.
     * 
     */
//    @Scheduled(cron = "0 */1 * * * ?")
    @PostConstruct
    public void perform() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }

}