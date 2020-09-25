package com.deviget.minesweeper.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.deviget.minesweeper.repository.GameRepository;
 
@Configuration
@EnableDynamoDBRepositories(basePackageClasses = GameRepository.class)
public class DynamoDBConfig {
 
	@Value("${amazon.aws.accesskey}")
	private String awsAccessKey;
 
	@Value("${amazon.aws.secretkey}")
	private String awsSecretKey;
 
	@Bean
	public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {
		@SuppressWarnings("deprecation")
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials);
		return amazonDynamoDB;
	}
 
	@Bean
	public AWSCredentials awsCredentials() {
		return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
	}
 
}