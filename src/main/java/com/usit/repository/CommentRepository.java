package com.usit.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	public Page<Comment> findByProductIdAndCommentTypeCd(Pageable pageRequest,int productId,String commentTypeCd);
	public Page<Comment> findByParentCommentIdIs(Pageable pageRequest,int parentCommentId);
	public long countByProductIdAndMemberId(int productId,int memberId);

	
}
