package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.PostingHistory;

@Repository
public interface PostingHistoryRepository extends JpaRepository<PostingHistory, Integer>{

	public List<PostingHistory> findByMemberIdOrderByPostingHistoryIdDesc(int memberId);


	
}
