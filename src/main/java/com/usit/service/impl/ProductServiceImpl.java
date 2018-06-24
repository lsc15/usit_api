package com.usit.service.impl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

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
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.repository.ProductOptionRepository;
import com.usit.repository.ProductRepository;
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
	
	

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	public Product getProduct(int productId) {
		
		
		return productRepository.findOne(productId);

	}

	public Page<Product> readAll(PageRequest pageRequest,String useYn,String tempYn) {
		
		if(useYn==null) {
			return productRepository.findAllByTempYn(pageRequest,tempYn);
		}else {
			return productRepository.findAllByUseYnAndTempYn(pageRequest,useYn,tempYn);
		}
		
		
		

	}
	
	public Page<Product> readAllByCategoryCdAndTempYn(PageRequest pageRequest,String categoryCd,String tempYn) {

		String useYn = "Y";
		return productRepository.findAllByCategoryCdAndUseYnAndTempYn(pageRequest,categoryCd,useYn,tempYn);
		
	}
	
	
	public Page<Product> readAllByRegIdAndDeleteYn(PageRequest pageRequest,Long RegId,String deleteYn) {

		return productRepository.findAllByRegIdAndDeleteYn(pageRequest,RegId,deleteYn);
		
	}
			

	//상품등록
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}
	
	

	//상품수정
	public Product updateProduct(Product product,int productId,Long memberId) {
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
			updateProduct.setSearchUseYn(product.getSearchUseYn());
			updateProduct.setTags(product.getTags());
			updateProduct.setSellMemberId(product.getSellMemberId());
			updateProduct.setUseYn(product.getUseYn());
			updateProduct.setDeleteYn(product.getDeleteYn());
			updateProduct.setTempYn(product.getTempYn());
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
	public ProductOption updateProductOption(ProductOption productOption, int productOptionId, Long memberId) {
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

	
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword); 
		}

	
}
