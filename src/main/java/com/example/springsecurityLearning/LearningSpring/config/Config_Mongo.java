package com.example.springsecurityLearning.LearningSpring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config_Mongo extends AbstractMongoClientConfiguration{
		@Value("${spring.data.mongodb.database}")
	    private String databaseName;
		
	    @Value("${spring.data.mongodb.uri}")
	    private String mongoDbUri;


	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return databaseName;
	}
	/*
	 * @Bean MongoTransactionManager transactionManager(MongoDatabaseFactory
	 * factory) { return new MongoTransactionManager(factory); }
	 */
	
	

}
