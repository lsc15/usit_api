package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.ShareHistory;
import com.usit.service.ShareHistoryService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/share-histories")
public class ShareController extends CommonHeaderController{


    @Autowired
    ShareHistoryService shareHistoryService;
    
    private static Logger LOGGER = LoggerFactory.getLogger(ShareController.class);

    
    /**
     * 공유내역 저장
     * @param request
     * @param params
     * @return 당일 최초발생건은 생성, 기발생건은 수정
     * @throws UnsupportedEncodingException 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws Exception
     */
    @PostMapping
	public ModelAndView saveShareHistory(@RequestBody ShareHistory shareHistory) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
     	ShareHistory share = new ShareHistory();
		share = shareHistoryService.getShareHistory(shareHistory.getProductId(),shareHistory.getStoreKey());
		
		try {
//        if(share != null) {
        	
//        	share = shareHistoryService.updateShareHistory(shareHistory, share.getShareId());
        
//        }else{
			if(share == null) {
        	share = shareHistoryService.createShareHistory(shareHistory);
			}
//        }
		}catch (Exception e) {
			LOGGER.warn("공유 히스토리 저장 실패.");
			throw new FrameworkException("-1001", "공유 히스토리 저장에 실패하였습니다."); // 오류 리턴 예시
		}

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", share);
		
		 return mav;
	}
    
    
    
    /**
     * 공유내역 조회
     * @param request
     * @param params
     * @return 해당토큰유저의 공유내용 전체
     * @throws UnsupportedEncodingException 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws Exception
     */
    @GetMapping("/token")
	public ModelAndView getShareHistory() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	List<ShareHistory> shareList = shareHistoryService.getShareHistoryByMemberId(sessionVO.getMemberId());
		

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", shareList);
		
		 return mav;
	}
    
    
    
    
    /**
     * 판매추천자 조회
     * @param request
     * @param params
     * @return 해당스토어키유저의 정보
     * @throws UnsupportedEncodingException 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws Exception
     */
    @GetMapping("/storekey")
	public ModelAndView getShareMember(@RequestParam("recommenderStoreKey") String recommenderStoreKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        

        
     	Member member = shareHistoryService.getShareMember(recommenderStoreKey);
		

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", member);
		
		 return mav;
	}
    
    
    
}
