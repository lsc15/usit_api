package com.usit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer>{

	ProductOption findFirstByProductIdAndSeq(int productId,int seq);
	
}
