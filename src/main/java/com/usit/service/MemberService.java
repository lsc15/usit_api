package com.usit.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.usit.domain.Member;
import com.usit.domain.VerifyToken;

public interface MemberService {
	Collection<GrantedAuthority> getAuthorities(String membername);

	Page<Member> readAll(PageRequest pageRequest);
	
	Member getMemberByEmail(String email);
	
	Member getMemberByMemeberId(Integer memberId);
	
	List<Object[]> getRecommenderList(Integer memberId);
	
	Member getMemberByUid(String Uid);

	Member createMember(Member member);
	
	Member modifyMember(Member member);
	
	
	VerifyToken createToken(VerifyToken token);

	VerifyToken getToken(VerifyToken token);

	PasswordEncoder passwordEncoder();
}

