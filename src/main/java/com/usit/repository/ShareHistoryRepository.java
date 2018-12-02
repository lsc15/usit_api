package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ShareHistory;

@Repository
public interface ShareHistoryRepository extends JpaRepository<ShareHistory, Integer>{

	public ShareHistory findByDateAndMemberIdAndProductId(String today, Integer memberId,int productId);
	
	public List<ShareHistory> findByMemberId(Integer memberId);
	
	
}
