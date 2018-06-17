package com.usit.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.web.CommonHeaderController;
import com.usit.service.AwsS3Service;

import lombok.RequiredArgsConstructor;

/**
 * @author seungcheol-i
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(FileController.class);
	
	@Autowired AwsS3Service service;
	
	
	@Autowired
    private Environment env;
	
	@PostMapping()
	public ModelAndView uploadFile(@RequestPart MultipartFile file, @RequestParam("path") String path) throws IOException {
		
		File convFile = convert(file);
		
		String prefix = "";
		
		if (path != null) prefix = path + "/";
		
		String fileName = prefix + String.valueOf(System.currentTimeMillis());

		service.uploadFile(convFile, fileName);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        mv.addObject("result_code", resultCode);
        mv.addObject("result_msg", resultMsg);
		mv.addObject("link", "https://s3.ap-northeast-2.amazonaws.com/storage.usit.co.kr/" + fileName);
		mv.addObject("data", "https://s3.ap-northeast-2.amazonaws.com/storage.usit.co.kr/" + fileName);
		
		return mv;
	}
	
	
	
	
	
	/**
	 * @title file delete
	 * @param key
	 * @return
	 * @throws IOException
	 */
	@DeleteMapping()
	public ModelAndView deleteFile(@RequestParam("path") String path) throws IOException {
		
		
		String resultCode = "0000";
        String resultMsg = "";
		
		try {
			service.deleteFile(path);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultCode = "-1001";
	        resultMsg = e.getMessage();
		}
		
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		
        
        mv.addObject("result_code", resultCode);
        mv.addObject("result_msg", resultMsg);
		
		return mv;
	}
	
	
	
	public File convert(MultipartFile file) throws IOException
	{    
	    File convFile = new File(System.getProperty("java.io.tmpdir") + "/"  + file.getOriginalFilename());
	    convFile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
	
}
