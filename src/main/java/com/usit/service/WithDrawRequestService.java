package com.usit.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.WithdrawRequest;

public interface WithDrawRequestService {

	WithdrawRequest createWithdrawRequest(WithdrawRequest withdrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException;
	
	Page<WithdrawRequest> readAll(Pageable pageable,String periodCondition,String startDate,String endDate,String keywordCondition,String keyword) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException;
	
	Page<WithdrawRequest> readAllByToken(Pageable pageable,Integer memberId) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException;
	
	WithdrawRequest modifyWithdrawRequest(WithdrawRequest withdrawRequest);
	
}