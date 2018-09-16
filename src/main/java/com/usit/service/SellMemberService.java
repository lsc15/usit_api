package com.usit.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usit.domain.SellMember;

public interface SellMemberService {
	Collection<GrantedAuthority> getAuthorities(String membername);

	
	Page<SellMember> readAll(PageRequest pageRequest);
	
	SellMember getMemberByMemeberId(Integer sellMemberId);

	SellMember createMember(SellMember sellMember);

	SellMember modifyMember(SellMember sellMember);

	PasswordEncoder passwordEncoder();
}

