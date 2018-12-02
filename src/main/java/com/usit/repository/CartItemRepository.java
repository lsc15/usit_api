package com.usit.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	public Page<CartItem> findByMemberIdOrderByCartItemIdDesc(Pageable page,int memberId);


	
}
