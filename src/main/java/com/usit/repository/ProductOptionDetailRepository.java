package com.usit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ProductOption;
import com.usit.domain.ProductOptionDetail;

@Repository
public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetail, Integer>{

	
}
