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
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;

	
	
	//상품등록
	@PostMapping
	public ModelAndView createProduct(@RequestBody Product product) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	String memberId = sessionVO.getMemberId();
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
	public ModelAndView modifyProduct(@RequestBody Product product, @PathVariable int productId) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
//     	if("on".equals(product.getBadgeTypeCd()) || "none".equals(product.getBadgeTypeCd())) {
//     		product.setBadgeTypeCd(null);
//     	}
     	
		Product updateProduct = productService.updateProduct(product,productId,sessionVO.getMemberId());
	

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
		
		
	//상품옵션삭제
//	@DeleteMapping("/produc 
	
	
	
	

	
	@GetMapping("/{productId}")
	public ModelAndView getProduct(@PathVariable int productId) { 

		ModelAndView mav = new ModelAndView("jsonView");
		String resultCode = "0000";
        String resultMsg = "";

        
        
		Product data = productService.getProduct(productId);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", data);
		
		 return mav;
	}
	
	//currentPage,PerPage를 받는

	@GetMapping
	public ModelAndView getProducts(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,@RequestParam(name ="useYn",required = false)  String useYn) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String tempYn = "N";
		Page<Product> page = productService.readAll(pageRequest,useYn,tempYn);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	@GetMapping("/category/{categoryCd}")
	public ModelAndView getProducts(@PathVariable String categoryCd, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "productId"));
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        String tempYn = "N";
        //useYn = Y
		Page<Product> page = productService.readAllByCategoryCdAndTempYn(pageRequest,categoryCd,tempYn);

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
        String deleteYn = "N";
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체
        SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
		Page<Product> page = productService.readAllByRegIdAndDeleteYn(pageRequest,sessionVO.getMemberId(),deleteYn);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}

}
