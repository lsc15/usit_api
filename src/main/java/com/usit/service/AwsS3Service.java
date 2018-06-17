package com.usit.service;

import java.io.File;
import java.util.Map;



public interface AwsS3Service {
	public void uploadFile(File uploadFile, String fileName);
	public void deleteFile(String fileName);
	
	
	
}
