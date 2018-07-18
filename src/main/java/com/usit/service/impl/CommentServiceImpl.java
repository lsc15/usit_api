package com.usit.service.impl;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.repository.OrderRepository;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.domain.Comment;
import com.usit.domain.Member;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderItem;
import com.usit.repository.CommentRepository;
import com.usit.repository.MemberRepository;
import com.usit.repository.UsitCodeRepository;
import com.usit.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private static Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);



	@Autowired
	CommentService commentService;
	
	
	@Autowired
	CommentRepository commentRepository;

	
	@Autowired
	MemberRepository memberRepository;
	
	
	@Autowired
	UsitCodeRepository usitCodeRepository;
	
	@Autowired
	OrderRepository orderRepository;
	

	
	public Page<Comment> getCommentList(Pageable pageRequest,int productId,String commentTypeCd) {
		
		return commentRepository.findByProductIdAndCommentTypeCd(pageRequest,productId,commentTypeCd);

	}
	
	public Page<Comment> getCommentListAll(Pageable pageRequest) {
		//parentCommentId가 없는 0인 결과만 select
		return commentRepository.findByParentCommentIdIs(pageRequest,0);

	}
	
	
	
	
	public boolean getCheckReview(int productId,int memberId) {

		boolean result = false;
		//review count
		long reviewCount = commentRepository.countByProductIdAndMemberId(productId,memberId);
		
		//order product count
		long productCount = 0 ;
		List<UsitOrder> list = orderRepository.findAllByMemberId(memberId);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			List <UsitOrderItem>orderItems = list.get(i).getOrderItems();
			for (Iterator<UsitOrderItem> iterator = orderItems.iterator(); iterator.hasNext();) {
				UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
				//배송완료인것
				if(usitOrderItem.getProductId() == productId && ("1204".equals(usitOrderItem.getDeliveryStatusCd()) ||"1207".equals(usitOrderItem.getDeliveryStatusCd()))   ) {
					productCount++;
				}
				
			}
		}
		
		result = reviewCount < productCount;
		
		return result;

	}
	
	
	

	public Comment createComment(Comment comment) {
		/*
		//리뷰작성시 포인트 50점
		if("1001".equals(comment.getCommentTypeCd())) {
			
			//리뷰포인트
			int point = Integer.parseInt(usitCodeRepository.findByMasterCdAndDetailCd("17","1700").getDetailCdNm());
			
			PointHistory pointHistory = new PointHistory();
			pointHistory.setAddPoint(point);
			pointHistory.setMemberId(comment.getMemberId());
			pointHistory.setPointTypeCd("1503");
			pointHistory.setRegId(comment.getMemberId());

			pointHistoryRepository.save(pointHistory);
			
			Member member = memberRepository.findOne(comment.getMemberId());
			member.setPoint(member.getPoint()+point);
			memberRepository.save(member);
		}
		*/
		return commentRepository.save(comment);
	}
	
	

	public Comment updateComment(Comment comment,int commentId,int memberId) {
		Comment updateComment = commentRepository.findOne(commentId);
		
		if(updateComment==null) {
			LOGGER.warn("해당 댓글이 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 댓글입니다"); // 오류 리턴 예시
		}else{
			
			updateComment.setModId(memberId);
			updateComment.setRate(comment.getRate());
			updateComment.setContent(comment.getContent());
		return commentRepository.save(updateComment);
			
		}
		
		
		
	}

	
	//댓글 삭제
		public void deleteComment(int commentId) {
			Comment comment = commentRepository.findOne(commentId);
			if(comment==null) {
				LOGGER.warn("해당 댓글이 없습니다.");
				throw new FrameworkException("-1001", "존재하지 않는 댓글입니다"); // 오류 리턴 예시
			}else{
				/*
				if("1001".equals(comment.getCommentTypeCd())) {
					
					//리뷰포인트
					int point = Integer.parseInt(usitCodeRepository.findByMasterCdAndDetailCd("17","1700").getDetailCdNm());
					
					PointHistory pointHistory = new PointHistory();
					pointHistory.setAddPoint(-point);
					pointHistory.setMemberId(comment.getMemberId());
					pointHistory.setPointTypeCd("1503");
					pointHistory.setRegId(comment.getMemberId());

					pointHistoryRepository.save(pointHistory);
					
					Member member = memberRepository.findOne(comment.getMemberId());
					member.setPoint(member.getPoint()-point);
					memberRepository.save(member);
				}
				*/
				
				commentRepository.delete(comment);
			}
			
			
		}
	

	
}
