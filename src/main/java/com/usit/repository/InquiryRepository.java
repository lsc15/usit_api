package com.usit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usit.domain.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
	public Page<Inquiry> findAll(Pageable pageRequest);
}
