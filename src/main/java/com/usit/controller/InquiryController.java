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
import com.usit.domain.Inquiry;
import com.usit.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiries")
public class InquiryController {
	
	@Autowired InquiryService inquiryService;

	@PostMapping()
	public ResponseEntity<JSONObject> postInquiry(@RequestBody Inquiry inquiry) {

		Inquiry result = inquiryService.createInquiry(inquiry);
		
		JSONObject jo = new JSONObject();
		
		jo.put("result_code", "0000");
		jo.put("result_msg", "정상 처리되었습니다.");
		jo.put("data", result);
		
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		
		return response;
	}
	
	@GetMapping()
	public ModelAndView getInquiries(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "inquiryId"));
		
		Page<Inquiry> result = inquiryService.readAll(pageRequest);
//		
//		return result;
		 
		 ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result_code", "0000");
		mv.addObject("result_msg", "정상 처리되었습니다.");
		mv.addObject("data", result);
//		
//		ResponseEntity<ModelAndView> response = new ResponseEntity<ModelAndView>(mv, HttpStatus.OK);
//		
		return mv;
	}
	

	@PutMapping("/{inquiryId}")
	public ModelAndView putInquiry(@RequestBody Inquiry inquiry,@PathVariable int inquiryId) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        Inquiry inquiryData = inquiryService.updateInquiry(inquiry);
     
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", inquiryData);
		
		 return mav;
	}
	
	
	
	
	@DeleteMapping("/{inquiryId}")
	public ModelAndView deleteInquiry(@PathVariable int inquiryId) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
		inquiryService.deleteInquiry(inquiryId);
		
		
		
	 	mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
//	    mav.addObject("data", result);
		
	    return mav;
	}
	
}
