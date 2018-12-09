package com.usit.controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.app.util.naverpay.NpayOrder;
import com.usit.app.util.naverpay.NpayZzim;
import com.usit.app.util.naverpay.NpayZzim.ItemStack;
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
	
	@Autowired
    private Environment env;

	
	
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
		 * @title 수정상품요청건 수정
		 * @param ApprovalProduct
		 * @return
		 */
		@PutMapping("/approval/{approvalId}")
		public ModelAndView modifyApproval(@PathVariable int approvalId, @RequestBody ApprovalProduct approvalProduct) {
			
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
	     	
	     	
	     	ApprovalProduct result = productService.modifyApprovalProduct(approvalProduct);
	        
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
			@RequestParam("productStatusCd") String productStatusCd, @RequestParam(name = "searchType",required = false, defaultValue = "") String searchType) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");

		String resultCode = "0000";
		String resultMsg = "";
		
		Page<Product> page = null;
		if("".equals(searchType)) {
			page = productService.readAll(pageRequest, productStatusCd);
		}else if(UsitCodeConstants.SEARCH_TYPE_POPULAR.equals(searchType)) {
			page = productService.readAllPopular(pageRequest, productStatusCd);
		}else if(UsitCodeConstants.SEARCH_TYPE_NEW.equals(searchType)) {
			page = productService.readAllNew(pageRequest, productStatusCd);
		}else if(UsitCodeConstants.SEARCH_TYPE_LOWEST.equals(searchType)) {
			page = productService.readAllLowest(pageRequest, productStatusCd);
		}
		else {
			LOGGER.warn("잘못된 요청입니다.");
			throw new FrameworkException("-1001", "잘못된 요청입니다."); // 오류 리턴 예시
		}
		

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
	public ModelAndView getProductsByCategory(@PathVariable String categoryCd, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
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
	 * @title badge별 상품조회
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping("/badge/{badgeTypeCd}")
	public ModelAndView getProductsBadge(@PathVariable String badgeTypeCd, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String temp = UsitCodeConstants.PRODUCT_STATUS_CD_ENROLL;
        //useYn = Y
		Page<Product> page = productService.readAllByBadgeTypeCdAndProductStatusCd(pageRequest,badgeTypeCd,temp);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}

	
	/**
	 * @title 고가 상품조회
	 * @param curPage
	 * @param perPage
	 * @return
	 */
	@GetMapping("/affluence")
	public ModelAndView getProductsAffluence(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "price"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String temp = UsitCodeConstants.PRODUCT_STATUS_CD_ENROLL;
        //useYn = Y
		Page<Product> page = productService.readAllByPrice(pageRequest,temp);

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
        
        
        
        
		Page<Product> page = productService.readAllBySellMemberIdAndProductStatusCdNot(pageRequest,sessionVO.getMemberId(),productStatusCdDelete);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	
	
	/**
	 * @return 
	 * @title 네이버페이 상품정보 연동
	 * 네이버페이는 비주기적으로 상품 정보를 업데이트하기 위해, 가맹점 사이트에 HTTP로 상품 정보 요청을 보낸다.
	 * @return xml
	 * @throws IOException 
	 * @throws Exception 
	 */
	@GetMapping("/productInfo")
	public void getNpayProducts(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String query = URLDecoder.decode( request.getQueryString(), UsitCodeConstants.USIT_ENCODE_UTF8);
        String pair [] = query.split("&");
        System.out.println("getQueryString:"+query);
        boolean supplementSearch = false;
        boolean optionSearch = false;
        int index = 0;
        ArrayList<Integer> requestList = new ArrayList<Integer>();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        
        
        // option 확인 상품갯수 산정
        for (String string : pair) {

        	if(string.contains(UsitCodeConstants.NAVER_PAY_SUPPLEMENTSEARCH)) {
        		if("true".equals(string.split("=")[1])) {
        			supplementSearch = true;
        		}
        		
        	}
        	if(string.contains(UsitCodeConstants.NAVER_PAY_OPTIONSEARCH)) {
        		if("true".equals(string.split("=")[1])) {
        			optionSearch = true;
        		}
        		
        	}
        	if(string.indexOf('[') != -1) {
        		int present = Integer.parseInt(string.substring(string.indexOf('[')+1,string.indexOf(']')));	
        	
        	if(index < present ) {
        		index = present;
        	}
        	}
			
		}
        index++;
        
        
//        		product[0][id]=122&
//        		product[0][optionManageCodes]=387&
//        		product[1][id]=135&
//        		product[1][optionManageCodes]=406,408&
//        		product[2][id]=146&
//        		product[3][id]=106&
//        		product[3][optionManageCodes]=336&
//        		product[4][id]=218&
//        		product[4][optionManageCodes]=728&
//        		supplementSearch=true&
//        		optionSearch=true
        
        int pairSize = pair.length;
        //int배열만큼 상품리스트를 만들고 hashMap을 통해 optionMansgeCode전달 예정
        for (int i = 0; i < pairSize; i++) {
        	
        	for(int innerCnt = 0; innerCnt < pairSize; innerCnt++ ) {
        	
        	if(pair[innerCnt].startsWith("product["+i+"][id]")) {
        		
        		requestList.add(Integer.parseInt(pair[innerCnt].split("=")[1]));
        		
        		
        		for(int j = 0; j < pairSize; j++) {
        			if(pair[j].startsWith("product["+i+"][optionManageCodes]")) {
                		map.put(Integer.parseInt(pair[innerCnt].split("=")[1]), pair[j].split("=")[1]);
                	}
        		}
        	}
        	}
		
		}
        
        
        
        
        
//        for (int i = 0; i < pairSize; i++) {
//        	
//        		
//        		
//        		for(int j = 0; j < pairSize; j++) {
//        			
//        			if(pair[j].startsWith("product["+i+"][id]")) {
//        				requestList.add(Integer.parseInt(pair[j].split("=")[1]));	
//        			}
//        			if(pair[j].startsWith("product["+i+"][optionManageCodes]")) {
//    					map.put(Integer.parseInt(pair[i].split("=")[1]), pair[j].split("=")[1]);
//    				}
//        			
//
//        	}
//        	
//        	
//		
//		}
        
        
        
        //product[0][id]=XXX&product[0][optionManageCodes]=X_X,X_X& product[0][supplementIds]=XXX&supplementSearch=true&optionSearch=true
        /**
         * product[0][id]=XXX&
         * product[0][optionManageCodes]=X_X,X_X&
         * product[0][supplementIds]=XXX&
         * supplementSearch=true&
         * optionSearch=true 
         */
        
        
//		Page<Product> page = productService.readAllBySellMemberIdAndProductStatusCdNot(pageRequest,sessionVO.getMemberId(),productStatusCdDelete);

        ArrayList<Product> productList = new ArrayList<Product>();
        
        for (Integer reqProduct : requestList) {
        	
        	Product product = productService.getProduct(reqProduct);
        	productList.add(product);
        	
			
		}
        
        NpayOrder no = new NpayOrder();
        String xmlData = null;
        try {
        	xmlData = no.generateNpayProduct(productList,map,supplementSearch,optionSearch);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//		mav.addObject("result_code", resultCode);
//        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", xmlData);
//         SAXBuilder builder = new SAXBuilder();
         
        response.setContentType("application/xml;charset=utf-8");
//        response.setContentType("application/html;charset=utf-8");
//        response.setHeader("Cache-Control", "no-cache");

//        response.getOutputStream().print(xmlData);
        ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(xmlData.getBytes(UsitCodeConstants.USIT_ENCODE_UTF8));
	        out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			out.close();
		}
        
        
//        ResponseEntity<String> responCseXml = new ResponseEntity<String>(xmlData, HttpStatus.OK);
//		return responseXml;
	}
	
	
//	@PostMapping()
//	public ResponseEntity<> postInquiry() {
//
//		
//		JSONObject jo = new JSONObject();
//		
//		jo.put("result_code", "0000");
//		jo.put("result_msg", "정상 처리되었습니다.");
//		jo.put("data", "12");
//		
//		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
//		r
//		return response;
//	}
	
	
	
	
	@GetMapping("/zzim")
	public ModelAndView naverZzimProduct(@RequestParam("productId") String productId) {

		ModelAndView mav = new ModelAndView("jsonView");
		String resultCode = "0000";
        String resultMsg = "";
        
        
        List<Integer> productIds = new ArrayList<Integer>();
        StringTokenizer stk = new StringTokenizer(productId, ",");
      
        while(stk.hasMoreTokens()) {
        	productIds.add(Integer.parseInt(stk.nextToken()));
        }
        
		List<Product> data = productService.readZzimProducts(productIds);
		
		
		String merchantId = env.getProperty("naver.npay.merchant.id");
	    String certiKey = env.getProperty("naver.npay.certikey");
	    String url = env.getProperty("naver.npay.zzim.dev.url");
		
		// 주문상품 내역으로 items 데이터를 생성한다.
				
	    List<ItemStack> items = new ArrayList<ItemStack>();
	    

	    for (Product product : data) {
	    	int price =0;
	    	if("Y".equals(product.getDiscountYn())) {
	    		price = product.getDiscountedPrice();
	    	}else {
	    		price = product.getPrice();
	    	}
	    	items.add(new ItemStack(String.valueOf(product.getProductId()), product.getTitle(), price, product.getTitleImg(),	UsitCodeConstants.USIT_PRODUCT_URL_PREFIX  + String.valueOf(product.getProductId())));
	    }
				
	    NpayZzim send;
		
	    String[] prodSeqs = null;
		
	    try {
		
	    	send = new NpayZzim(url);
			
	    	prodSeqs = send.sendZzimToNC(merchantId, certiKey, items.toArray(new ItemStack[0]));
			
	    } catch (IOException e) {
		
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		
	    // 여기서 얻은prodSeqs로 zzim popup을 띄운다.
		
	    System.out.println(Arrays.toString(prodSeqs));
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", prodSeqs);
        return mav;
	}
	

}
