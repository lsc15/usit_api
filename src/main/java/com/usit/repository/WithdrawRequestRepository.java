package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.WithdrawRequest;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequest, Integer>{


	public List<WithdrawRequest> findAllByMemberId(Integer memberId);
	
	
}
