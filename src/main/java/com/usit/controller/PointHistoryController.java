package com.usit.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.PointHistory;
import com.usit.service.PointHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point-histories")
public class PointHistoryController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(PointHistoryController.class);

	@Autowired
	PointHistoryService pointHistoryService;

	
	
	@PostMapping
	public ModelAndView addPoint(@RequestBody PointHistory point) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	point.setRegId(sessionVO.getMemberId());
     	PointHistory result = new PointHistory();
     	result = pointHistoryService.addPoint(point);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
	

	
	@GetMapping("/member/{memberId}")
	public ModelAndView getCartItems(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,@PathVariable int memberId) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "pointId"));

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        

        
        Page<PointHistory> page = pointHistoryService.getPointListByMemberId(pageRequest,memberId);
        
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	



}
