package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.Product;
import com.usit.domain.ProductOption;

public interface ProductService {

	Product getProduct(int productId);

	Page<Product> readAll(PageRequest pageRequest,String useYn,String tempYn);
	
	Page<Product> readAllByCategoryCdAndTempYn(PageRequest pageRequest,String categoryCd,String tempYn);
	
	Page<Product> readAllByRegIdAndDeleteYn(PageRequest pageRequest,Long RegId,String deleteYn);
	
	void deleteProduct(int productId);
	
//	void deleteProductOption(int productId);
	
	Product createProduct(Product product);
	
	ProductOption createProductOption(ProductOption productOption);
	
	Product updateProduct(Product product,int productId,Long memberId);
	
	ProductOption updateProductOption(ProductOption productOption,int productId,Long memberId);


}