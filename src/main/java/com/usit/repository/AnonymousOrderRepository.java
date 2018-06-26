package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.AnonymousOrder;
import com.usit.domain.CartItem;

@Repository
public interface AnonymousOrderRepository extends JpaRepository<AnonymousOrder, Integer>{

//	public List<AnonymousOrder> findByMemberIdOrderByAnonymousOrderIdDesc(Long memberId);


	
}
