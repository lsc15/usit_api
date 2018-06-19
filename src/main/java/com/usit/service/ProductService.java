package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.Product;
import com.usit.domain.ProductOption;

public interface ProductService {

	Product getProduct(int productId);

	Page<Product> readAll(PageRequest pageRequest,String useYn);
	
	Page<Product> readAllByCategoryCd(PageRequest pageRequest,String categoryCd);
	
	Page<Product> readAllByRegIdAndDeleteYn(PageRequest pageRequest,String RegId,String deleteYn);
	
	void deleteProduct(int productId);
	
//	void deleteProductOption(int productId);
	
	Product createProduct(Product product);
	
	ProductOption createProductOption(ProductOption productOption);
	
	Product updateProduct(Product product,int productId,String memberId);
	
	ProductOption updateProductOption(ProductOption productOption,int productId,String memberId);


}