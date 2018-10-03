package com.usit.repository;


import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	public Page<Comment> findByProductIdAndCommentTypeCd(Pageable pageRequest,int productId,String commentTypeCd);
	public Page<Comment> findBySellMemberIdAndCommentTypeCdAndRegDateGreaterThanEqualAndRegDateLessThanEqualAndParentCommentIdIsNull(Pageable pageRequest,int sellMemberId,String commentTypeCd,LocalDateTime startDate,LocalDateTime endDate
			);
	public Page<Comment> findByCommentTypeCdAndRegDateGreaterThanEqualAndRegDateLessThanEqualAndParentCommentIdIsNull(Pageable pageRequest,String commentTypeCd,LocalDateTime startDate,LocalDateTime endDate
			);
	public Page<Comment> findBySellMemberIdAndCommentTypeCdAndChildCntGreaterThanEqualAndRegDateGreaterThanEqualAndRegDateLessThanEqualAndParentCommentIdIsNull(Pageable pageRequest,int sellMemberId,String commentTypeCd,int ChildCnt,LocalDateTime startDate,LocalDateTime endDate
			);
	public Page<Comment> findByCommentTypeCdAndChildCntGreaterThanEqualAndRegDateGreaterThanEqualAndRegDateLessThanEqualAndParentCommentIdIsNull(Pageable pageRequest,String commentTypeCd,int ChildCnt,LocalDateTime startDate,LocalDateTime endDate
			);
	public long countByProductIdAndCommentTypeCdAndMemberId(int productId,String commentTypeCd,Integer memberId);

	
}
