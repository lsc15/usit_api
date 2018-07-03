package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Comment;

public interface CommentService {

	Page<Comment> getCommentList(Pageable pageRequest,int productId,String commentTypeCd);
	
	Page<Comment> getCommentListAll(Pageable pageRequest);
	
	boolean getCheckReview(int productId,Long memberId);

	Comment createComment(Comment comment);

	Comment updateComment(Comment comment,int commentId,Long memberId);

	void deleteComment(int commentId);


}