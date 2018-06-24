package com.usit.service;

import java.util.List;


import com.usit.domain.CartItem;

public interface CartItemService {

	List<CartItem> getCartItemsList(Long memberId);

	CartItem createCartItem(CartItem cartItem);

	CartItem updateCartItem(CartItem cartItem,int cartItemId,Long memberId);

	void deleteCartItem(int cartItemId);


}