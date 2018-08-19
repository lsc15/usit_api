package com.usit.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Comment;
import com.usit.domain.SellMember;
import com.usit.service.CommentService;
import com.usit.service.SellMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	CommentService commentService;

	@Autowired
    SellMemberService sellMemberService;
	
	@PostMapping
	public ModelAndView createComment(@RequestBody Comment comment) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
    		
     	comment.setRegId(sessionVO.getMemberId());
    		Comment newComment = new Comment();
    		newComment=commentService.createComment(comment);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", newComment);
		
		 return mav;
	}
	
	

	@PutMapping("/{commentId}")
	public ModelAndView updateComment(@RequestBody Comment comment,@PathVariable int commentId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		Comment updateComment = new Comment();
		updateComment = commentService.updateComment(comment,commentId,sessionVO.getMemberId());
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", updateComment);
		
		 return mav;
	}
	
	
	@DeleteMapping("/{commentId}")
	public ModelAndView deleteComment(@PathVariable int commentId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        commentService.deleteComment(commentId);
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", cartItem);
		
		 return mav;
	}

	
	@GetMapping("/product/{productId}/type/{commentTypeCd}")
	public ModelAndView getComments(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,@PathVariable int productId,@PathVariable String commentTypeCd) { 
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "commentId"));

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
		Page<Comment> page = commentService.getCommentList(pageRequest,productId,commentTypeCd);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	@GetMapping
	public ModelAndView getCommentsAdmin(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,@RequestParam("commentTypeCd") String commentTypeCd,
			@RequestParam(name = "replyYn", defaultValue = "", required = false) String replyYn,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate) { 
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "commentId"));

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
     	Page<Comment> page;
     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
     		page = commentService.getCommentListAll(pageRequest,sessionVO.getMemberId(),commentTypeCd,replyYn,startDate,endDate);
     	}else {
     		page = commentService.getCommentListAllAdmin(pageRequest,commentTypeCd,replyYn,startDate,endDate);
     	}
     	
     	
		

		
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	
	
	@GetMapping("/{commentId}")
	public ModelAndView getReview(@PathVariable int commentId) { 
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
		Comment result = commentService.getReview(commentId);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
	
	
	
	@GetMapping("/is-review-writeable")
	public ModelAndView getCheckReview(@RequestParam("productId") int productId) { 
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		boolean result = commentService.getCheckReview(productId, sessionVO.getMemberId());

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}



}
