package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Comment;

public interface CommentService {

	Comment getReview(int commentId);
	
	Page<Comment> getCommentList(Pageable pageRequest,int productId,String commentTypeCd);
	
	Page<Comment> getCommentListAll(Pageable pageRequest,int sellMemberId,String commentTypeCd,String replyYn,String startDate,String endDate);
	
	Page<Comment> getCommentListAllAdmin(Pageable pageRequest,String commentTypeCd,String replyYn,String startDate,String endDate);
	
	boolean getCheckReview(int productId,int memberId);

	Comment createComment(Comment comment);

	Comment updateComment(Comment comment,int commentId,int memberId);

	void deleteComment(int commentId);


}