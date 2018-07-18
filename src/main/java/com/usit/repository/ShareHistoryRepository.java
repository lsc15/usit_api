package com.usit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ShareHistory;

@Repository
public interface ShareHistoryRepository extends JpaRepository<ShareHistory, Integer>{

	public ShareHistory findByDateAndMemberId(String today, Integer memberId);

	
}
