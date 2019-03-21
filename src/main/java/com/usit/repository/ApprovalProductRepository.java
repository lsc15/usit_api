package com.usit.repository;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ApprovalProduct;

@Repository
public interface ApprovalProductRepository extends JpaRepository<ApprovalProduct, Integer>{

	
	public Page<ApprovalProduct> findAllByApprovalStatusCd(Pageable pageRequest,String approvalStatusCd);
	
	public List<ApprovalProduct> findByProductId(Integer productId);
	
	
	public ApprovalProduct findTop1ByProductIdOrderByApprovalProductIdDesc(Integer productId);
	

	

	
}
