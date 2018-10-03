package com.usit.service.impl;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.domain.ApprovalProduct;
import com.usit.domain.ApprovalProductOption;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.domain.SellMember;
import com.usit.repository.ApprovalProductOptionRepository;
import com.usit.repository.ApprovalProductRepository;
import com.usit.repository.ProductOptionRepository;
import com.usit.repository.ProductRepository;
import com.usit.repository.SellMemberRepository;
import com.usit.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

	private static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);



	
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductOptionRepository productOptionRepository;
	
	@Autowired
	ApprovalProductRepository approvalProductRepository;
	
	@Autowired
	ApprovalProductOptionRepository approvalProductOptionRepository;
	
	
	@Autowired
	SellMemberRepository sellMemberRepository;
	
	

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	public Product getProduct(int productId) {
		
		
		return productRepository.findOne(productId);

	}

	public Page<Product> readAll(PageRequest pageRequest,String productStatusCd) {
		
			return productRepository.findAllByProductStatusCd(pageRequest,productStatusCd);

	}
	
	public Page<Product> readAllByPrice(PageRequest pageRequest,String productStatusCd) {
		
		return productRepository.findByProductStatusCdOrderByPriceDesc(pageRequest,productStatusCd);

	}
	
	
	public Page<Product> readAllNew(PageRequest pageRequest,String productStatusCd) {
		
		return productRepository.findAllByProductStatusCdAndNewYn(pageRequest,productStatusCd,"Y");

	}
	
	public Page<Product> readAllPopular(PageRequest pageRequest,String productStatusCd) {
		
		return productRepository.findAllByProductStatusCdAndPopularYn(pageRequest,productStatusCd,"Y");

	}
	
	public Page<Product> readAllLowest(PageRequest pageRequest,String productStatusCd) {
		
		return productRepository.findAllByProductStatusCdAndLowestYn(pageRequest,productStatusCd,"Y");

	}
	
	
	
	public Page<Product> readAllByCategoryCdAndProductStatusCd(PageRequest pageRequest,String categoryCd,String productStatusCd) {

		return productRepository.findAllByCategoryCdAndProductStatusCd(pageRequest,categoryCd,productStatusCd);
		
	}
	
	
	public Page<Product> readAllByBadgeTypeCdAndProductStatusCd(PageRequest pageRequest,String badgeTypeCd,String productStatusCd) {

		return productRepository.findAllByBadgeTypeCdAndProductStatusCd(pageRequest,badgeTypeCd,productStatusCd);
		
	}
	
	
	public Page<Product> readAllBySellMemberIdAndProductStatusCdNot(PageRequest pageRequest,int sellMemberId,String productDelete) {

		
		SellMember sellMember =  sellMemberRepository.getOne(sellMemberId);
		
		if(sellMember.getMemberTypeCd().equals(UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER)) {
		return productRepository.findAllByProductStatusCdNot(pageRequest,productDelete);
		}else {
		return productRepository.findAllBySellMemberIdAndProductStatusCdNot(pageRequest,sellMemberId,productDelete);
		}
	
	}
			

	//상품등록
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}
	
	

	//상품수정
	public Product updateProduct(Product product,int productId,int memberId) {
		Product updateProduct = productRepository.findOne(productId);
		
		if(updateProduct==null) {
			LOGGER.warn("해당 상품이 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 상품입니다"); // 오류 리턴 예시
		}else{
			
			
			updateProduct.setTitle(product.getTitle());
			updateProduct.setCategoryCd(product.getCategoryCd());
			updateProduct.setDescription(product.getDescription());
			updateProduct.setPrice(product.getPrice());
			updateProduct.setDiscountYn(product.getDiscountYn());
			updateProduct.setDiscountedPrice(product.getDiscountedPrice());
			updateProduct.setDeliveryPrice(product.getDeliveryPrice());
			updateProduct.setDeliveryPriceCut(product.getDeliveryPriceCut());
			updateProduct.setInventoryUseYn(product.getInventoryUseYn());
			updateProduct.setInventory(product.getInventory());
			updateProduct.setOptionUseYn(product.getOptionUseYn());
			updateProduct.setTitleImg(product.getTitleImg());
			updateProduct.setAdditionalImgs(product.getAdditionalImgs());
			updateProduct.setDetailImgs(product.getDetailImgs());
			updateProduct.setDetailContent(product.getDetailContent());
			updateProduct.setDetailImgUseYn(product.getDetailImgUseYn());
			updateProduct.setBadgeTypeCd(product.getBadgeTypeCd());
			updateProduct.setSearchUseYn(product.getSearchUseYn());
			updateProduct.setNewYn(product.getNewYn());
			updateProduct.setPopularYn(product.getPopularYn());
			updateProduct.setLowestYn(product.getLowestYn());
			updateProduct.setTags(product.getTags());
			updateProduct.setInquiryPhone(product.getInquiryPhone());
			updateProduct.setSellMemberId(product.getSellMemberId());
			updateProduct.setProductStatusCd(product.getProductStatusCd());
			updateProduct.setDeliveryCompanyCd(product.getDeliveryCompanyCd());
			updateProduct.setDeliveryRuralPrice(product.getDeliveryRuralPrice());
			updateProduct.setDeliveryJejuPrice(product.getDeliveryJejuPrice());
			updateProduct.setCommissionPct(product.getCommissionPct());
			updateProduct.setModId(memberId);
			
			return productRepository.save(updateProduct);
		}
		
		
	}
	
	
	
	
	//상품삭제
	public void deleteProduct(int productId) {
		Product product = productRepository.findOne(productId);
		if(product==null) {
			LOGGER.warn("해당 상품이 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 상품입니다"); // 오류 리턴 예시
		}else{
			productRepository.delete(product);
		}
		
		
	}
	
	
	
	
	

	//상품상세등록
	public ProductOption createProductOption(ProductOption productOption) {
//		productOption.setSoldoutYn("N");
//		productOption.setUseYn("Y");
		return productOptionRepository.save(productOption);
	}
	
	
	

	//상품상세수정
	public ProductOption updateProductOption(ProductOption productOption, int productOptionId, int memberId) {
		ProductOption updateProductOption = productOptionRepository.findOne(productOptionId);
		
		if(updateProductOption==null) {
			LOGGER.warn("해당 상품이 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 상품입니다"); // 오류 리턴 예시
		}else{
			updateProductOption.setOptionName1(productOption.getOptionName1());
			updateProductOption.setOptionValue1(productOption.getOptionValue1());
			updateProductOption.setOptionName2(productOption.getOptionName2());
			updateProductOption.setOptionValue2(productOption.getOptionValue2());
			updateProductOption.setAddPrice(productOption.getAddPrice());
			updateProductOption.setInventory(productOption.getInventory());
			updateProductOption.setAvailableYn(productOption.getAvailableYn());
			updateProductOption.setDeleteYn(productOption.getDeleteYn());
			updateProductOption.setUseYn(productOption.getUseYn());
//			updateProductOption.setProductId(productOption.getProductId());
			updateProductOption.setSeq(productOption.getSeq());
			updateProductOption.setModId(memberId);
			updateProductOption = productOptionRepository.save(updateProductOption);
		}
		
		return updateProductOption;
		
	}
	
	
	
	
	
//	public void deleteProductOption(int productId) {
//		ProductOption productOption = productOptionRepository.findOne(productId);
//		if(productOption==null) {
//			LOGGER.warn("해당 상품옵션이 없습니다.");
//			throw new FrameworkException("-1001", "존재하지 않는 상품옵션입니다"); // 오류 리턴 예시
//		}else{
//			productOptionRepository.delete(productOption);
//		}
//		
//		
//	}

	
	
	
	
	
	
	public Page<ApprovalProduct> findAllByApprovalStatusCd(PageRequest pageRequest,String approvalProductStatusCd) {
	
		return approvalProductRepository.findAllByApprovalStatusCd(pageRequest,approvalProductStatusCd);
	}
	
	
	public ApprovalProduct findApprovalProduct(int approvalProductId) {
		
		return approvalProductRepository.findOne(approvalProductId);
	}
	
	
		
	//수정상품등록
	public ApprovalProduct createApprovalProduct(ApprovalProduct approvalProduct) {
		return approvalProductRepository.save(approvalProduct);
	}
		
		
		
	// 수정상품 확인
	public void disableApprovalProduct(ApprovalProduct approvalProduct) {
		List<ApprovalProduct> list = approvalProductRepository.findByProductId(approvalProduct.getProductId());

		for (ApprovalProduct approval : list) {
			approval.setApprovalStatusCd(UsitCodeConstants.APPROVAL_STATUS_CD_DENY);
		}

	}
		
	// 수정상품옵션 확인
	public void disableApprovalProductOption(ApprovalProductOption approvalProductOption) {
		List<ApprovalProductOption> optionList = approvalProductOptionRepository.findByProductId(approvalProductOption.getProductId());
		for (ApprovalProductOption approvalOption : optionList) {
			approvalOption.setApprovalStatusCd(UsitCodeConstants.APPROVAL_STATUS_CD_DENY);
		}

	}

	// 수정상품상세등록
	public ApprovalProductOption createApprovalProductOption(ApprovalProductOption approvalProductOption) {
		return approvalProductOptionRepository.save(approvalProductOption);
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	
}
