package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.domain.SellMember;
import com.usit.domain.VerifyToken;
import com.usit.service.CommonService;
import com.usit.service.MemberService;
import com.usit.service.PointHistoryService;
import com.usit.service.SellMemberService;
import com.usit.util.MailUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	MemberService memberService;
	
	@Autowired
	SellMemberService sellMemberService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	PointHistoryService pointHistoryService;

	@Autowired
	AuthenticationManager authenticationManager;


	
	@PostMapping
	public ModelAndView postUser(@RequestBody Member member) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
		

        
        /**
         * 기본 50% 커미션
         */
        member.setCommissionPct(50);
        
        
		Member result = memberService.createMember(member);
		 
		 
		//가입 이벤트
		PointHistory point = new PointHistory();
		point.setAddPoint(10000);
		point.setBalancePoint(10000);
		point.setPointTypeCd(UsitCodeConstants.POINT_TYPE_CD_EVENT);
		point.setMemberId(result.getMemberId());
		pointHistoryService.addPoint(point);
		 
		mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
	    mav.addObject("data", result);
			
		return mav;
		//email, password, name, genderCd, birthDate, mobile, postcode, address, addressDetail
	}
	
	
	
	@PutMapping("/{userId}")
	public ModelAndView modifyUser(@RequestBody Member member) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
		
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
		Member result = memberService.modifyMember(member);
		 
		 
		 
		mav.addObject("result_code", resultCode);
	    mav.addObject("result_msg", resultMsg);
	    mav.addObject("data", result);
			
		return mav;
		//email, password, name, genderCd, birthDate, mobile, postcode, address, addressDetail
	}
	
	
	 
	//인증메일 발송
//	@PostMapping("/send-verify-email")
//	public ModelAndView sendVerifyEmail(@RequestBody VerifyToken token) {
//
//		ModelAndView mav = new ModelAndView("jsonView");
//		
//		String resultCode = "0000";
//        String resultMsg = "";
//		
//        
////        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체
//
////     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
//		
//		
//		MailUtil mu = new MailUtil();
//		String key = mu.getKey(15, false);
//		token.setToken(key);
//		token.setType("email");
////		token.setRegId(sessionVO.getMemberId());
//
//		VerifyToken vf = memberService.createToken(token);
//		try {
//			mu.mail(token.getEmail(), key);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		mav.addObject("result_code", resultCode);
//	    mav.addObject("result_msg", resultMsg);
//	    mav.addObject("data", vf);
//			
//		return mav;
//	}
	
	
	
	

	//currentPage,PurposePage를 받는

	@GetMapping
	public ModelAndView getUsers(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {

		
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "memberId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	
     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
        
     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
     		LOGGER.warn("권한이 없습니다.");
			throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
     	}
        
		Page<Member> page = memberService.readAll(pageRequest);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		return mav;
	}

	@GetMapping("/email")
	public ModelAndView getUser(@RequestParam(name = "email") String email) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

		Member member = memberService.getMemberByEmail(email);
		
		
		boolean isMember = false;
		
		if(member!=null && member.getEmail().equals(email)) {
			isMember=true;
		}

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", isMember);
		return mav;
	}
	
	
	@GetMapping("/{memberId}")
	public ModelAndView getUserMemberId(@PathVariable("memberId") int memberId) {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

		Member member = memberService.getMemberByMemeberId(memberId);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", member);
		return mav;
	}
	
	
	
	@PostMapping("/token")
	public ModelAndView getUseInfo(@RequestBody VerifyToken token) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        BaseAuthenticationSuccessHandler maas = new BaseAuthenticationSuccessHandler();
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
		Member member = memberService.getMemberByMemeberId(sessionVO.getMemberId());
		LOGGER.debug("!!!!:" +sessionVO.getMemberId());
		AES256Util aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		member.setStoreKey(aes256Util.encrypt(member.getMemberUid()));
		
//        AES256Util aes256Util = new AES256Util(maas.getKey());
//        String plainToken = aes256Util.decrypt(token.getToken());
//        String [] tokenArr = plainToken.split(":");
        
//        if(tokenArr.length > 1 && !tokenArr[0].isEmpty()) {
//		Member member = memberService.getMemberByMemeberId(Integer.parseInt(tokenArr[0]));
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", member);
//        }else {
//          	LOGGER.warn("@@@@@@@@@@@정상적인 접근이 아닙니다.");
//			throw new FrameworkException("-1001", "해당 토큰은 유효하지 않습니다."); // 오류 리턴 예시
//        }

		return mav;
		
	}

	
	
	@PostMapping("/welcome")
	public ModelAndView getWelcomeMsg(@RequestBody Member member) throws Exception  {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        BaseAuthenticationSuccessHandler maas = new BaseAuthenticationSuccessHandler();
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
		Member verifyMember = memberService.getMemberByMemeberId(member.getMemberId());
		
		if(verifyMember==null) {
			//인증실패
			LOGGER.warn("인증에 실패하였습니다.");
			throw new FrameworkException("-1001", "해당 사용자는 유효하지 않습니다."); // 오류 리턴 예시
			
		}else {
			//인증성공
			String variable [] = new String [1];
			variable[0] = verifyMember.getName();
			//카카오알림톡 발송
			int status = commonService.sendAlimtalk("U001",verifyMember.getMobile(),variable);
			LOGGER.info("kakaoStatus : "+status);
		}
		
		
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", member);

		return mav;
		
	}
	
	



}
