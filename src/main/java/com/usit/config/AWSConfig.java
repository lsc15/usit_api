package com.usit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
public class AWSConfig {

	@Value("AKIAIH6MQ44P6C7Q4IOQ")
	private String awsId;
	
	@Value("FcpU1lGr1JIXCM3IZso4iNN7/w5gHoj2vQAwVJlb")
	private String awsKey;
	
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new Resource[] {new ClassPathResource("/amazon.properties")});
		return ppc;
	}

	@Bean
	public AWSCredentials credential() {
		return new BasicAWSCredentials(awsId, awsKey);
	}

	@Bean
	public AmazonS3 s3client() {
		return new AmazonS3Client(credential());
	}

}
