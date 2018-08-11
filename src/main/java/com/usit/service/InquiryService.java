package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Inquiry;

public interface InquiryService {
	public Inquiry createInquiry(Inquiry inquiry);
	public Page<Inquiry> readAll(Pageable pageable);

	public Inquiry updateInquiry(Inquiry inquiry);
	public void deleteInquiry(int inquiryId);
	
}
