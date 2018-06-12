package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Comment;

public interface CommentService {

	Page<Comment> getCommentList(Pageable pageRequest,int productId,String commentTypeCd);
	
	Page<Comment> getCommentListAll(Pageable pageRequest);
	
//	boolean getCheckReview(int productId,int memberId);

//	Comment createComment(Comment comment);

	Comment updateComment(Comment comment,int commentId,String memberId);

//	void deleteComment(int commentId);


}