package com.usit.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.CartItem;
import com.usit.domain.Member;
import com.usit.service.CartItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartItemController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(CartItemController.class);

	@Autowired
	CartItemService cartItemService;

	
	
	@PostMapping
	public ModelAndView createCartItem(@RequestBody CartItem cartItem) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
    		
    		cartItem.setRegId(sessionVO.getMemberId());
    		CartItem newCart = new CartItem();
    		newCart=cartItemService.createCartItem(cartItem);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", newCart);
		
		 return mav;
	}
	
	

	@PutMapping("/{cartItemId}")
	public ModelAndView updateCartItem(@RequestBody CartItem cartItem,@PathVariable int cartItemId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		CartItem updateCartItem = new CartItem();
		updateCartItem = cartItemService.updateCartItem(cartItem,cartItemId,sessionVO.getMemberId());
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", updateCartItem);
		
		 return mav;
	}
	
	
	@DeleteMapping("/{cartItemId}")
	public ModelAndView deleteCartItem(@PathVariable int cartItemId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
		cartItemService.deleteCartItem(cartItemId);
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", cartItem);
		
		 return mav;
	}

	
	@GetMapping("/member/{memberId}")
	public ModelAndView getCartItems(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage, @PathVariable Integer memberId) {

		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "cartItemId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        Page<CartItem> page = cartItemService.getCartItemsList(pageRequest,memberId);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	



}
