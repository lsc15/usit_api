package com.usit.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.ApprovalProduct;
import com.usit.domain.ApprovalProductOption;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;

public interface ProductService {

	Product getProduct(int productId);

	
	List<Product> readZzimProducts(List<Integer> productIds);
	
	Page<Product> readAll(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllByPrice(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllNew(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllLowest(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllPopular(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllByCategoryCdAndProductStatusCd(PageRequest pageRequest,String categoryCd,String productStatusCd);
	
	Page<Product> readAllByBadgeTypeCdAndProductStatusCd(PageRequest pageRequest,String badgeTypeCd,String productStatusCd);
	
	Page<Product> readAllBySellMemberIdAndProductStatusCdNot(PageRequest pageRequest,int sellMemberId,String productDelete);
	
	void deleteProduct(int productId);
	
//	void deleteProductOption(int productId);
	
	Product createProduct(Product product);
	
	ProductOption createProductOption(ProductOption productOption);
	
	Product updateProduct(Product product,int productId,int memberId);
	
	ProductOption updateProductOption(ProductOption productOption,int productId,int memberId);
	
	

	//수정심사요청
	
	Page<ApprovalProduct> findAllByApprovalStatusCd(PageRequest pageRequest,String approvalProductStatusCd);
	
	ApprovalProduct findApprovalProduct(int approvalProductId);
	
	ApprovalProduct findTop1ByProductIdOrderByApprovalProductIdDesc(int productId);
	
	ApprovalProduct modifyApprovalProduct(ApprovalProduct approvalProduct);
	
	ApprovalProduct createApprovalProduct(ApprovalProduct approvalProduct);
	
	void disableApprovalProduct(ApprovalProduct approvalProduct);
	
	void disableApprovalProductOption(ApprovalProductOption approvalProductOption);
	
	ApprovalProductOption createApprovalProductOption(ApprovalProductOption approvalProductOption);
	


}