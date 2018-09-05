package com.usit.controller;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.ApprovalProduct;
import com.usit.domain.ApprovalProductOption;
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.domain.SellMember;
import com.usit.domain.ShareHistory;
import com.usit.service.ProductService;
import com.usit.service.SellMemberService;
import com.usit.service.ShareHistoryService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;
	
	@Autowired
    SellMemberService sellMemberService;
	
	@Autowired
	ShareHistoryService shareHistoryService;

	
	
	//상품등록
	@PostMapping
	public ModelAndView createProduct(@RequestBody Product product) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	int memberId = sessionVO.getMemberId();
     	product.setRegId(memberId);
     	
     	
     	
     	
//     	if("on".equals(product.getBadgeTypeCd()) || "none".equals(product.getBadgeTypeCd())) {
//     		product.setBadgeTypeCd(null);
//     	}
     	
     	
     	
     	
     	Product result = productService.createProduct(product);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}

	//상품수정
	@PutMapping("/{productId}")
	public ModelAndView modifyProduct(@RequestBody Product product, @PathVariable int productId, @RequestParam(name = "removeApproval", required = false) String removeApproval) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
//     	if("on".equals(product.getBadgeTypeCd()) || "none".equals(product.getBadgeTypeCd())) {
//     		product.setBadgeTypeCd(null);
//     	}
     	
		Product updateProduct = productService.updateProduct(product,productId,sessionVO.getMemberId());
		
		//상품수정 요청의 경우 심사상품, 옵션 모두 삭제처리 
		if("true".equals(removeApproval)) {
			ApprovalProduct approvalProduct = new ApprovalProduct();
			ApprovalProductOption approvalProductOption = new  ApprovalProductOption();
			approvalProduct.setProductId(productId);
			approvalProductOption.setProductId(productId);
			
			productService.disableApprovalProduct(approvalProduct);
			productService.disableApprovalProductOption(approvalProductOption);
		}
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", updateProduct);
		
		 return mav;
	}
	
	//상품삭제
	@DeleteMapping("/{productId}")
	public ModelAndView deleteProduct(@PathVariable int productId) {
		

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
		productService.deleteProduct(productId);
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", "");
		
		 return mav;
	}
	
	
	
	
    //상품옵션등록
	@PostMapping("/product-options")
	public ModelAndView createProducOption(@RequestBody ProductOption productOption) {
			
		ModelAndView mav = new ModelAndView("jsonView");
			
		String resultCode = "0000";
		String resultMsg = "";
	        
		SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
		
     	productOption.setRegId(sessionVO.getMemberId());
     	
	    	ProductOption result = productService.createProductOption(productOption);
	        
	       mav.addObject("result_code", resultCode);
	       mav.addObject("result_msg", resultMsg);
	       mav.addObject("data", result);
			
		 return mav;
	}
	
	
	

	//상품옵션수정
	@PutMapping("/product-options/{productOptionId}")
	public ModelAndView modifyProductOption(@RequestBody ProductOption productoption,@PathVariable int productOptionId) {
			
		ModelAndView mav = new ModelAndView("jsonView");
			
		String resultCode = "0000";
        String resultMsg = "";
	        
	        
	    SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

    	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
	        
		ProductOption updateProductOption = productService.updateProductOption(productoption,productOptionId,sessionVO.getMemberId());
	

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", updateProductOption);
			
		 return mav;
	}
		
		
	
	
	
	
	

		/**
		 * @title 수정상품요청건 등록
		 * @param ApprovalProduct
		 * @return
		 */
		@PostMapping("/approval")
		public ModelAndView createApprovalProduct(@RequestBody ApprovalProduct approvalProduct) {
			
			ModelAndView mav = new ModelAndView("jsonView");
			
			String resultCode = "0000";
	        String resultMsg = "";
	        

	     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

	     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
	     	int memberId = sessionVO.getMemberId();
	     	approvalProduct.setRegId(memberId);
	     	
	     	productService.disableApprovalProduct(approvalProduct);
	     	
	     	
	     	ApprovalProduct result = productService.createApprovalProduct(approvalProduct);
	        
	        mav.addObject("result_code", resultCode);
	        mav.addObject("result_msg", resultMsg);
	        mav.addObject("data", result);
			
			 return mav;
		}
		
		
		
		/**
		 * @title 수정상품옵션 등록
		 * @param ApprovalProductOption
		 * @return
		 */
		@PostMapping("/product-options/approval")
		public ModelAndView createApprovalProducOption(@RequestBody ApprovalProductOption approvalProductOption) {
				
			ModelAndView mav = new ModelAndView("jsonView");
				
			String resultCode = "0000";
			String resultMsg = "";
		        
			SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

	     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
			
	     	approvalProductOption.setRegId(sessionVO.getMemberId());
	     	
	     	productService.disableApprovalProductOption(approvalProductOption);
		    
	     	ApprovalProductOption result = productService.createApprovalProductOption(approvalProductOption);
		    
	     	mav.addObject("result_code", resultCode);
	     	mav.addObject("result_msg", resultMsg);
	     	mav.addObject("data", result);
	     	return mav;
		}
		
		
		/**
		 * @title 수정상품요청건 관리자 목록조회
		 * @param curPage
		 * @param perPage
		 * @return
		 */
		@GetMapping("/approval")
		public ModelAndView getApprovalProducts(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
			
			ModelAndView mav = new ModelAndView("jsonView");
			
			String resultCode = "0000";
	        String resultMsg = "";
	        PageRequest pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "approvalProductId");

	        String status = UsitCodeConstants.APPROVAL_STATUS_CD_APPROVAL;
	        
	     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

	     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
	     	
	     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
	        
	     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
	     		LOGGER.warn("권한이 없습니다.");
				throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
	     	}
	     	
	     	
	     	Page<ApprovalProduct> list = productService.findAllByApprovalStatusCd(pageRequest,status);
	        
	        mav.addObject("result_code", resultCode);
	        mav.addObject("result_msg", resultMsg);
	        mav.addObject("data", list);
			
			 return mav;
		}
		
		
		/**
		 * @title 수정상품요청건 관리자 단건조회
		 * @param approvalProductId
		 * @return
		 */
		@GetMapping("/approval/{approvalProductId}")
		public ModelAndView getApprovalProductList(@PathVariable int approvalProductId) {
			
			ModelAndView mav = new ModelAndView("jsonView");
			
			String resultCode = "0000";
	        String resultMsg = "";

	     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

	     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
	     	
	     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
	        
	     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
	     		LOGGER.warn("권한이 없습니다.");
				throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
	     	}
	     	
	     	
	     	ApprovalProduct data = productService.findApprovalProduct(approvalProductId);
	        
	        mav.addObject("result_code", resultCode);
	        mav.addObject("result_msg", resultMsg);
	        mav.addObject("data", data);
			
			 return mav;
		}
	
	
		
	/**
	 * @title 상품단건 조회 공유상품의 경우 view 증가
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping("/{productId}")
	public ModelAndView getProduct(@PathVariable int productId,
			@RequestParam(name = "storeKey", required = false) String storeKey) {

		ModelAndView mav = new ModelAndView("jsonView");
		String resultCode = "0000";
		String resultMsg = "";

		// shareHistory visit생성

		if (storeKey != null) {

			ShareHistory share = shareHistoryService.getShareHistory(productId, storeKey);
			try {
				if (share != null) {
					share = shareHistoryService.updateShareHistory(productId, share.getShareId());

				} else {
					share = shareHistoryService.createShareHistory(productId, storeKey);
				}
			} catch (Exception e) {
				LOGGER.warn("공유 히스토리 저장 실패.");
				throw new FrameworkException("-1001", "공유 히스토리 저장에 실패하였습니다."); // 오류 리턴 예시
			}

		}

		Product data = productService.getProduct(productId);

		mav.addObject("result_code", resultCode);
		mav.addObject("result_msg", resultMsg);
		mav.addObject("data", data);

		return mav;
	}
	
		
	/**
	 * @title 상품의 상태별 조회목록
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping
	public ModelAndView getProducts(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
			@RequestParam("productStatusCd") String productStatusCd) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");

		String resultCode = "0000";
		String resultMsg = "";
		Page<Product> page = productService.readAll(pageRequest, productStatusCd);

		mav.addObject("result_code", resultCode);
		mav.addObject("result_msg", resultMsg);
		mav.addObject("data", page);

		return mav;
	}	
	
	/**
	 * @title 카레고리별 상품조회
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping("/category/{categoryCd}")
	public ModelAndView getProducts(@PathVariable String categoryCd, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String temp = UsitCodeConstants.PRODUCT_STATUS_CD_ENROLL;
        //useYn = Y
		Page<Product> page = productService.readAllByCategoryCdAndProductStatusCd(pageRequest,categoryCd,temp);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}


	
	
	/**
	 * @title 사용자 계정으로 등록된 상품목록
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping("/token")
	public ModelAndView getTokenProducts(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String productStatusCdDelete = UsitCodeConstants.PRODUCT_STATUS_CD_DELETE;
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체
        SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
        
        
        
		Page<Product> page = productService.readAllByRegIdAndProductStatusCdNot(pageRequest,sessionVO.getMemberId(),productStatusCdDelete);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}

}
