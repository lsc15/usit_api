package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.Product;
import com.usit.domain.ProductOption;

public interface ProductService {

	Product getProduct(int productId);

	Page<Product> readAll(PageRequest pageRequest,String productStatusCd);
	
	Page<Product> readAllByCategoryCdAndProductStatusCdNot(PageRequest pageRequest,String categoryCd,String productStatusCd);
	
	Page<Product> readAllByRegIdAndProductStatusCdNot(PageRequest pageRequest,int RegId,String productDelete);
	
	void deleteProduct(int productId);
	
//	void deleteProductOption(int productId);
	
	Product createProduct(Product product);
	
	ProductOption createProductOption(ProductOption productOption);
	
	Product updateProduct(Product product,int productId,int memberId);
	
	ProductOption updateProductOption(ProductOption productOption,int productId,int memberId);


}