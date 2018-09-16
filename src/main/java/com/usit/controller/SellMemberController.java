package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.security.handler.BaseAuthenticationSuccessHandler;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.SellMember;
import com.usit.domain.VerifyToken;
import com.usit.service.CommonService;
import com.usit.service.MemberService;
import com.usit.service.SellMemberService;
import com.usit.util.MailUtil;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sell-users")
public class SellMemberController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(SellMemberController.class);

	@Autowired
	SellMemberService sellMemberService;
	
	@Autowired
	CommonService commonService;;

	@Autowired
	AuthenticationManager authenticationManager;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	@PostMapping
	public ModelAndView postUser(@RequestBody SellMember member) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
		
        
//        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

//     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
//     	member.setReg_id(sessionVO.getMemberId());
		SellMember result = sellMemberService.createMember(member);
		 
		 
		 
		mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
	    mav.addObject("data", result);
			
		return mav;
		//email, password, name, genderCd, birthDate, mobile, postcode, address, addressDetail
	}
	
	
	
	@GetMapping
	public ModelAndView getUsers(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {

		
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "sellMemberId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
		Page<SellMember> page = sellMemberService.readAll(pageRequest);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		return mav;
	}
	
	
	
	@PutMapping("/{userId}")
	public ModelAndView modifyUser(@RequestBody SellMember sellMember) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
		
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	sellMember.setModId(sessionVO.getMemberId());
     	SellMember result = sellMemberService.modifyMember(sellMember);
		 
		 
		 
		mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
	    mav.addObject("data", result);
			
		return mav;
		//email, password, name, genderCd, birthDate, mobile, postcode, address, addressDetail
	}
	
	

	
	@PostMapping("/token")
	public ModelAndView getUseInfor(@RequestBody VerifyToken token) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		SellMember sellMember = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", sellMember);

		return mav;
		
		
	}

	
	
	



}
