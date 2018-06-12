package com.usit.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.usit.service.AwsS3Service;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

   @Autowired
   private AmazonS3 s3client;
	
   
   
   @Value("storage.usit.co.kr")
   private String nameCardBucket;
   
   @Value("result.usit.co.kr")
   private String nameCardBucketResult;
   
   /*
    * upload file to folder and set it to public
    */ 
    public void uploadFile(File file, String filename) {
		
	String fileNameInS3 = filename;
	
	s3client.putObject(new PutObjectRequest(nameCardBucket, fileNameInS3, file)
			.withCannedAcl(CannedAccessControlList.PublicRead));
    }
    
     

}
