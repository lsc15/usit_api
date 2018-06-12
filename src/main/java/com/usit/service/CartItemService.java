package com.usit.service;

import java.util.List;


import com.usit.domain.CartItem;

public interface CartItemService {

	List<CartItem> getCartItemsList(String memberId);

	CartItem createCartItem(CartItem cartItem);

	CartItem updateCartItem(CartItem cartItem,int cartItemId,String memberId);

	void deleteCartItem(int cartItemId);


}