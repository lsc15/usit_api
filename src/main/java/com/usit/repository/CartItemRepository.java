package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	public List<CartItem> findByMemberIdOrderByCartItemIdDesc(Long memberId);


	
}
