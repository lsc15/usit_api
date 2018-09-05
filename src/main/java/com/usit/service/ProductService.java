package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.ApprovalProduct;
import com.usit.domain.ApprovalProductOption;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;

public interface ProductService {

	Product getProduct(int productId);

	Page<Product> readAll(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllByCategoryCdAndProductStatusCd(PageRequest pageRequest,String categoryCd,String productStatusCd);
	
	Page<Product> readAllByRegIdAndProductStatusCdNot(PageRequest pageRequest,int RegId,String productDelete);
	
	void deleteProduct(int productId);
	
//	void deleteProductOption(int productId);
	
	Product createProduct(Product product);
	
	ProductOption createProductOption(ProductOption productOption);
	
	Product updateProduct(Product product,int productId,int memberId);
	
	ProductOption updateProductOption(ProductOption productOption,int productId,int memberId);
	
	

	//수정심사요청
	
	Page<ApprovalProduct> findAllByApprovalStatusCd(PageRequest pageRequest,String approvalProductStatusCd);
	
	ApprovalProduct findApprovalProduct(int approvalProductId);
	
	ApprovalProduct createApprovalProduct(ApprovalProduct approvalProduct);
	
	void disableApprovalProduct(ApprovalProduct approvalProduct);
	
	void disableApprovalProductOption(ApprovalProductOption approvalProductOption);
	
	ApprovalProductOption createApprovalProductOption(ApprovalProductOption approvalProductOption);
	


}