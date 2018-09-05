package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ApprovalProductOption;

@Repository
public interface ApprovalProductOptionRepository extends JpaRepository<ApprovalProductOption, Integer>{

	public List<ApprovalProductOption> findByProductId(Integer productId);
}
