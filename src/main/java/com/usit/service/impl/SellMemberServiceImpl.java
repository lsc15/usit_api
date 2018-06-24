package com.usit.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.domain.SellMember;
import com.usit.domain.VerifyToken;
import com.usit.repository.MemberRepository;
import com.usit.repository.SellMemberRepository;
import com.usit.repository.VerifyTokenRepository;
import com.usit.service.SellMemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SellMemberServiceImpl implements SellMemberService {

	private static Logger LOGGER = LoggerFactory.getLogger(SellMemberServiceImpl.class);


	@Autowired
	SellMemberRepository sellMemberRepository;
	
	@Autowired
	VerifyTokenRepository verifyTokenRepository;


	@Autowired SellMemberService sellMemberService;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public Collection<GrantedAuthority> getAuthorities(String membername) {
		List<String> string_authorities = new ArrayList<String>();  //authorityRepository.findAuthoritiesByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String authority : string_authorities) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
	}


	
	
	public SellMember getMemberByMemeberId(Long memberId) {
		return sellMemberRepository.findOne(memberId);
	}
	



	public SellMember createMember(SellMember member) {
		SellMember existMember = sellMemberRepository.findOne(member.getSellMemberId());
		if(existMember!=null) {
			LOGGER.warn("이미 등록된 사용자입니다.");
			throw new FrameworkException("-1001", "이미 등록된 사용자입니다."); // 오류 리턴 예시
		}else{
		return sellMemberRepository.save(member);
		}
	}
	
	
	
	
	public SellMember modifyMember(SellMember sellMember) {
		SellMember updateMember = sellMemberRepository.findOne(sellMember.getSellMemberId());
//		String encodedPassword = passwordEncoder.encode(member.getPassword());
		updateMember.setName(sellMember.getName());
		updateMember.setMobile(sellMember.getMobile());
		updateMember.setEmail(sellMember.getEmail());
		updateMember.setCompanyNm(sellMember.getCompanyNm());
		updateMember.setRepresentorNm(sellMember.getRepresentorNm());
		updateMember.setBusinessNo(sellMember.getBusinessNo());
		updateMember.setBusinessAddress(sellMember.getBusinessAddress());
		updateMember.setBusinessAddressDetail(sellMember.getBusinessAddressDetail());
		updateMember.setBusinessPostcode(sellMember.getBusinessPostcode());
		updateMember.setBusinessConditions(sellMember.getBusinessConditions());
		updateMember.setBusinessType(sellMember.getBusinessType());
		updateMember.setBusinessClassCd(sellMember.getBusinessClassCd());
		updateMember.setOnlineBusinessNo(sellMember.getOnlineBusinessNo());
		updateMember.setReturnAddress(sellMember.getReturnAddress());
		updateMember.setReturnAddressDetail(sellMember.getReleaseAddressDetail());
		updateMember.setReturnPostcode(sellMember.getReleasePostcode());
		updateMember.setReturnPhone(sellMember.getReleasePhone());
		updateMember.setRepresentCategoryCd(sellMember.getRepresentCategoryCd());
		updateMember.setReleaseAddress(sellMember.getReleaseAddress());
		updateMember.setReleaseAddressDetail(sellMember.getReleaseAddressDetail());
		updateMember.setReleasePostcode(sellMember.getReleasePostcode());
		updateMember.setReleasePhone(sellMember.getReleasePhone());
		updateMember.setModId(sellMember.getModId());
		return sellMemberRepository.save(updateMember);
	}



	
	


	@Override
	public PasswordEncoder passwordEncoder() {
		return this.passwordEncoder;
	}



}
