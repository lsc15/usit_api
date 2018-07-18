package com.usit.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.domain.CartItem;
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.repository.CartItemRepository;
import com.usit.repository.MemberRepository;
import com.usit.service.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

	private static Logger LOGGER = LoggerFactory.getLogger(CartItemServiceImpl.class);



	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;



	
	public List<CartItem> getCartItemsList(int memberId) {
		
		return cartItemRepository.findByMemberIdOrderByCartItemIdDesc(memberId);

	}
	

	public CartItem createCartItem(CartItem cartItem) {
		
		return cartItemRepository.save(cartItem);
	}
	
	

	public CartItem updateCartItem(CartItem cartItem,int cartItemId,int memberId) {
		CartItem updateCartItem = cartItemRepository.findOne(cartItemId);
		
		if(updateCartItem==null) {
			LOGGER.warn("해당 장바구니가 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 장바구니입니다"); // 오류 리턴 예시
		}else{
			updateCartItem.setMemberId(cartItem.getMemberId());
			updateCartItem.setProductId(cartItem.getProductId());
			updateCartItem.setProductOptionId(cartItem.getProductOptionId());
			updateCartItem.setQuantity(cartItem.getQuantity());
			updateCartItem.setModId(memberId);
		
		return cartItemRepository.save(updateCartItem);
			
		}
		
		
		
	}

	
	//장바구니 삭제
		public void deleteCartItem(int cartItemId) {
			CartItem cartItem = cartItemRepository.findOne(cartItemId);
			if(cartItem==null) {
				LOGGER.warn("해당 장바구니가 없습니다.");
				throw new FrameworkException("-1001", "존재하지 않는 장바구니입니다"); // 오류 리턴 예시
			}else{
				cartItemRepository.delete(cartItemId);;
			}
			
			
		}
	

	
}
