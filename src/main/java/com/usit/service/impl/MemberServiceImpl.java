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

import com.usit.domain.Member;
import com.usit.domain.VerifyToken;
import com.usit.repository.MemberRepository;
import com.usit.repository.VerifyTokenRepository;
import com.usit.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private static Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);


	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	VerifyTokenRepository verifyTokenRepository;


	@Autowired MemberService memberService;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public Collection<GrantedAuthority> getAuthorities(String membername) {
		List<String> string_authorities = new ArrayList<String>();  //authorityRepository.findAuthoritiesByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String authority : string_authorities) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
	}


	public Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
	
	
	public Member getMemberByMemeberId(Integer memberId) {
		return memberRepository.findOne(memberId);
	}
	

	public Page<Member> readAll(PageRequest pageRequest) {
//		List<Member> list = memberMapper.getMemberList();
		return memberRepository.findAll(pageRequest);

	}


	public Member createMember(Member member) {
		return memberRepository.save(member);
	}
	
	
	public Member modifyMember(Member member) {
		Member updateMember = memberRepository.findOne(member.getMemberId());
//		String encodedPassword = passwordEncoder.encode(member.getPassword());
		
		
		updateMember.setName(member.getName());
		updateMember.setMobile(member.getMobile());
		updateMember.setPostcode(member.getPostcode());
		updateMember.setAddress(member.getAddress());
		updateMember.setAddressDetail(member.getAddressDetail());
//		updateMember.setAddress(member.getAddress());
//		updateMember.setAddressDetail(member.getAddressDetail());
//		updateMember.setBirthDate(member.getBirthDate().replaceAll("-", ""));
//		updateMember.setEmail(member.getEmail());
//		updateMember.setEmailVerifyYn(member.getEmailVerifyYn());
//		updateMember.setFailCount(member.getFailCount());
//		updateMember.setGenderCd(member.getGenderCd());
//		updateMember.setMobile(member.getMobile());
//		updateMember.setMobileVerifyYn(member.getMobileVerifyYn());
//		updateMember.setMod_id(member.getMod_id());
//		updateMember.setName(member.getName());
//		updateMember.setReg_id(member.getReg_id());
//		updateMember.setMemo(member.getMemo());
//		updateMember.setPassword(encodedPassword);
//		updateMember.setPostcode(member.getPostcode());
		return memberRepository.save(updateMember);
	}
	
	
	
	
	

	public VerifyToken createToken(VerifyToken token) {
		return verifyTokenRepository.save(token);
	}


	public VerifyToken getToken(VerifyToken token) {
		return verifyTokenRepository.findByEmailAndToken(token.getEmail(),token.getToken());
	}



	@Override
	public PasswordEncoder passwordEncoder() {
		return this.passwordEncoder;
	}



}
