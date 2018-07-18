package com.usit.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.SellMember;

@Repository
public interface SellMemberRepository extends JpaRepository<SellMember, Integer>{

	public SellMember findBySellMemberUid(String uid);
	public SellMember findByEmail(String email);
	
}