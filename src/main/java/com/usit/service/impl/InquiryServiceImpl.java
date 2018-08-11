package com.usit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.domain.Inquiry;
import com.usit.repository.InquiryRepository;
import com.usit.service.InquiryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	
	@Autowired InquiryRepository inquiryRepository;

	@Override
	public Inquiry createInquiry(Inquiry inquiry) {
		return inquiryRepository.save(inquiry);
	}

	@Override
	public Page<Inquiry> readAll(Pageable pageable) {
		return inquiryRepository.findAll(pageable);
	}
	
	
	
	public Inquiry updateInquiry(Inquiry inquiry) {
		Inquiry updateInquiry = inquiryRepository.findOne(inquiry.getInquiryId());
		updateInquiry.setStateCd(inquiry.getStateCd());
		Inquiry result = inquiryRepository.save(updateInquiry);
		return result;
	}
	
	public void deleteInquiry(int inquiryId) {
		inquiryRepository.delete(inquiryId);
	}

}
