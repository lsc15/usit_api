package com.usit.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usit.domain.SellMember;

public interface SellMemberService {
	Collection<GrantedAuthority> getAuthorities(String membername);

	SellMember getMemberByMemeberId(Long sellMemberId);

	SellMember createMember(SellMember sellMember);

	SellMember modifyMember(SellMember sellMember);

	PasswordEncoder passwordEncoder();
}

