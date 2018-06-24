package com.usit.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.domain.CartItem;
import com.usit.domain.Member;
import com.usit.domain.PostingHistory;
import com.usit.domain.Product;
import com.usit.repository.MemberRepository;
import com.usit.repository.PostingHistoryRepository;
import com.usit.service.PostingHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingHistoryServiceImpl implements PostingHistoryService{

	private static Logger LOGGER = LoggerFactory.getLogger(PostingHistoryServiceImpl.class);



	@Autowired
	PostingHistoryRepository postingHistoryRepository;



	
	public List<PostingHistory> getPostingHistoryList(Long memberId) {
		
		return postingHistoryRepository.findByMemberIdOrderByPostingHistoryIdDesc(memberId);

	}
	

	public PostingHistory createPostingHistory(PostingHistory postingHistory) {
		
		return postingHistoryRepository.save(postingHistory);
	}
	
	

	public PostingHistory updatePostingHistory(PostingHistory postingHistory,int postingHistoryId,Long memberId) {
		PostingHistory updatePostingHistory = postingHistoryRepository.findOne(postingHistoryId);
		
		if(updatePostingHistory==null) {
			LOGGER.warn("해당 히스토리가 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 히스토리입니다"); // 오류 리턴 예시
		}else{
			updatePostingHistory.setUrl(postingHistory.getUrl());
			updatePostingHistory.setMemberId(postingHistory.getMemberId());
			updatePostingHistory.setProductId(postingHistory.getProductId());
			updatePostingHistory.setModId(memberId);
			updatePostingHistory.setConfirmYn(postingHistory.getConfirmYn());
		return postingHistoryRepository.save(updatePostingHistory);
			
		}
		
		
		
	}

	
	    //히스토리 삭제
		public void deletePostingHistory(int postingHistoryId) {
			PostingHistory postingHistory = postingHistoryRepository.findOne(postingHistoryId);
			if(postingHistory==null) {
				LOGGER.warn("해당 히스토리가 없습니다.");
				throw new FrameworkException("-1001", "존재하지 않는 히스토리입니다"); // 오류 리턴 예시
			}else{
				postingHistoryRepository.delete(postingHistoryId);;
			}
			
			
		}
	

	
}
