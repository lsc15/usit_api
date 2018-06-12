package com.usit.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.PostingHistory;
import com.usit.domain.Product;
import com.usit.domain.UsitCode;
import com.usit.domain.VerifyToken;
import com.usit.service.CommonService;
import com.usit.service.PostingHistoryService;
import com.usit.util.CrawlingUtil;
import com.usit.util.InstagramUtil;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/postings")
public class PostingController extends CommonHeaderController{


    @Autowired
    PostingHistoryService postingHistoryService;
    
    private static Logger LOGGER = LoggerFactory.getLogger(PostingController.class);

    
    /**
     * 인스타그램 피드검증
     * @param request
     * @param params
     * @return 인스타그램의 경우 feed 체크
     * @throws Exception
     */
    @PostMapping
	public ModelAndView postSocialHistory(@RequestBody PostingHistory postingHistory) {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
        InstagramUtil hu = new InstagramUtil();
        PostingHistory post = new PostingHistory();
        boolean verify = false;
        
        
//        try{
        	
        if(UsitCodeConstants.POSTING_SNS_INSTAGRAM.equals(postingHistory.getSocialTypeCd())) {
        verify = hu.getInstagramFeed(postingHistory.getUrl());
        }
//        }catch (Exception e) {
//        	e.printStackTrace();
//        	LOGGER.warn("포스트 히스토리 저장 실패.");
//			throw new FrameworkException("-1001", "포스트 히스토리 저장에 실패하였습니다."); // 오류 리턴 예시
//        }
        
        if(verify || !UsitCodeConstants.POSTING_SNS_INSTAGRAM.equals(postingHistory.getSocialTypeCd())) {
        	
        	postingHistory.setMemberId(sessionVO.getMemberId());
        	postingHistory.setRegId(sessionVO.getMemberId());
        	postingHistory.setProductId(postingHistory.getProductId());
        	postingHistory.setUrl(postingHistory.getUrl());
        	postingHistory.setSocialTypeCd(postingHistory.getSocialTypeCd());
        	postingHistory.setSequence(1);
        	
        post = postingHistoryService.createPostingHistory(postingHistory);
        
        }else{
        	LOGGER.warn("포스트 히스토리 저장 실패.");
			throw new FrameworkException("-1001", "포스트 히스토리 저장에 실패하였습니다."); // 오류 리턴 예시
        }

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", post);
		
		 return mav;
	}

    
    
    
    
    
    
    /**
     * 인스타그램 피드검증
     * @param request
     * @param params
     * @return 인스타그램의 경우 feed 체크
     * @throws Exception
     */
    @PostMapping("ra")
	public ModelAndView crawling(@RequestBody PostingHistory postingHistory) {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        CrawlingUtil cu = new CrawlingUtil();
        
        Document verify = cu.getDocument(postingHistory.getUrl());

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", verify.toString());
		
		 return mav;
	}
    
    
    
    
    
    //포스팅수정
  	@PutMapping("/{productId}")
  	public ModelAndView modifyPosting(@RequestBody PostingHistory postingHistory, @PathVariable int postingHistoryId) {
  		
  		ModelAndView mav = new ModelAndView("jsonView");
  		
  		String resultCode = "0000";
          String resultMsg = "";
          
          
          SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

       	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴

       	
       	PostingHistory updatePostingHistory = postingHistoryService.updatePostingHistory(postingHistory,postingHistoryId,sessionVO.getMemberId());
  	

  		  mav.addObject("result_code", resultCode);
          mav.addObject("result_msg", resultMsg);
          mav.addObject("data", updatePostingHistory);
  		
  		 return mav;
  	}

    
    
    
    


}
