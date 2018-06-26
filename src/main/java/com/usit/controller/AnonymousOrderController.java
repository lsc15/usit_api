package com.usit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.AnonymousOrder;
import com.usit.domain.CartItem;
import com.usit.service.AnonymousOrderService;
import com.usit.service.CartItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/anonymous-order")
public class AnonymousOrderController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(AnonymousOrderController.class);

	@Autowired
	AnonymousOrderService anonymousOrderService;

	
	
	@PostMapping
	public ModelAndView createOrder(HttpServletRequest request,@RequestBody AnonymousOrder anonymousOrder) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     
     	anonymousOrder.setUserIp(request.getRemoteAddr());
     	
    		
     	AnonymousOrder newOrder = new AnonymousOrder();
     	newOrder=anonymousOrderService.createAnonymousOrder(anonymousOrder);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", newOrder);
		
		 return mav;
	}
	
	

	@PutMapping("/{anonymousOrderId}")
	public ModelAndView updateCartItem(@RequestBody AnonymousOrder anonymousOrder,@PathVariable int anonymousOrderId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		AnonymousOrder updateAnonymousOrder = new AnonymousOrder();
		updateAnonymousOrder = anonymousOrderService.updateAnonymousOrder(anonymousOrder,anonymousOrderId,sessionVO.getMemberId());
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", updateAnonymousOrder);
		
		 return mav;
	}
	
	
	@DeleteMapping("/{anonymousOrderId}")
	public ModelAndView deleteAnonymousOrder(@PathVariable int anonymousOrderId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        anonymousOrderService.deleteAnonymousOrder(anonymousOrderId);
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", cartItem);
		
		 return mav;
	}

	
//	@GetMapping("/member/{memberId}")
//	public ModelAndView getAnonymousOrder(@PathVariable Long memberId) {
//
//		ModelAndView mav = new ModelAndView("jsonView");
//		
//		String resultCode = "0000";
//        String resultMsg = "";
//        
//		List<AnonymousOrder> list = anonymousOrderService.getAnonymousOrder(memberId);
//
//		mav.addObject("result_code", resultCode);
//        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", list);
//		
//		 return mav;
//	}
	
	



}
