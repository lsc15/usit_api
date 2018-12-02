package com.usit.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.domain.DeliveryFee;
import com.usit.domain.Inquiry;
import com.usit.domain.PointHistory;
import com.usit.service.DeliveryFeeService;
import com.usit.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-fees")
public class DeliveryFeeController {
	
	@Autowired DeliveryFeeService deliveryFeeService;

	@PostMapping
	public ModelAndView createDeliveryFee(@RequestBody DeliveryFee deliveryFee) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        
     	DeliveryFee result = deliveryFeeService.createDeliveryFee(deliveryFee);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
	

	@PutMapping("/{deliveryFeeId}")
	public ModelAndView putDeliveryFee(@RequestBody DeliveryFee deliveryFee,@PathVariable int deliveryFeeId) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        DeliveryFee deliveryFeeData = deliveryFeeService.updateDeliveryFee(deliveryFee);
     
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", deliveryFeeData);
		
		 return mav;
	}
	
	
	
	
	@DeleteMapping("/{deliveryFeeId}")
	public ModelAndView deleteDeliveryFee(@PathVariable int deliveryFeeId) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        deliveryFeeService.deleteDeliveryFee(deliveryFeeId);
		
		
		
	 	mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
//	    mav.addObject("data", result);
		
	    return mav;
	}
	
}
