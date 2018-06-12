package com.usit.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfiguration {

	
	
	@PostConstruct
	public void init() {

		/**
		 * https://firebase.google.com/docs/server/setup
		 * 
		 * Create service account , download json
		 */
	
		try {
		FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/usit-84ed4-firebase-adminsdk-80lmr-71cba28584.json").getInputStream()))
              .setDatabaseUrl("https://usit-84ed4.firebaseio.com/")
              .build();
            FirebaseApp.initializeApp(options);
            
            
            
   		FirebaseOptions secondaryOptions = new FirebaseOptions.Builder()
   			  .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/usitshop-firebase-adminsdk-44vhk-e1be039f88.json").getInputStream()))
              .setDatabaseUrl("https://usitshop.firebaseio.com/")
    	      .build();
    	     FirebaseApp.initializeApp(secondaryOptions,"secondary");
            
            
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
//		InputStream inputStream = FirebaseConfig.class.getClassLoader().getResourceAsStream(configPath);
//
//		FirebaseOptions options = new FirebaseOptions.Builder().setServiceAccount(inputStream)
//				.setDatabaseUrl(databaseUrl).build();
//		FirebaseApp.initializeApp(options);
		
	}
	
}
