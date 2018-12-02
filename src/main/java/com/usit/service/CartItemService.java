package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.CartItem;

public interface CartItemService {

	Page<CartItem> getCartItemsList(Pageable page,int memberId);

	CartItem createCartItem(CartItem cartItem);

	CartItem updateCartItem(CartItem cartItem,int cartItemId,int memberId);

	void deleteCartItem(int cartItemId);


}