package com.usit.service.impl;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.util.SessionVO;
import com.usit.domain.Member;
import com.usit.domain.MemberRole;
import com.usit.domain.SellMember;
import com.usit.repository.MemberRepository;
import com.usit.repository.SellMemberRepository;
import com.usit.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private static Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    SellMemberRepository sellMemberRepository;

    public SessionVO getLoginMember(String id) {
        Member member = memberRepository.findOne(id);
        LOGGER.info("user:{}", member);
        SessionVO sessionVO = null;

        if(member != null) {
            sessionVO = new SessionVO();
            sessionVO.setMemberId(member.getMemberId());
            sessionVO.setMemberEmail(member.getEmail());
            sessionVO.setMemberName(member.getName());
//            sessionVO.setPassword(member.getPassword());
            sessionVO.setMemberRoles(member.getMemberRoles().stream().map(MemberRole::getRoleId).collect(Collectors.toList()));
        }

        return sessionVO;
    }
    
    public SessionVO getLoginShopMember(String id) {
        SellMember sellMember = sellMemberRepository.findOne(id);
        LOGGER.info("selluser:{}", sellMember);
        SessionVO sessionVO = null;

        if(sellMember != null) {
            sessionVO = new SessionVO();
            sessionVO.setMemberId(sellMember.getSellMemberId());
            sessionVO.setMemberEmail(sellMember.getEmail());
            sessionVO.setMemberName(sellMember.getName());
            sessionVO.setMemberRoles(sellMember.getMemberRoles().stream().map(MemberRole::getRoleId).collect(Collectors.toList()));
        }

        return sessionVO;
    }


    public SessionVO getLoginMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        LOGGER.info("user:{}", member);
        SessionVO sessionVO = null;

        if(member != null) {
            sessionVO = new SessionVO();
            sessionVO.setMemberId(member.getMemberId());
            sessionVO.setMemberEmail(member.getEmail());
            sessionVO.setMemberName(member.getName());
//            sessionVO.setPassword(member.getPassword());
            sessionVO.setMemberRoles(member.getMemberRoles().stream().map(MemberRole::getRoleId).collect(Collectors.toList()));
        }

        return sessionVO;
    }
    
    
    
    public SessionVO getLoginShopMemberByEmail(String email) {
        SellMember sellMember = sellMemberRepository.findByEmail(email);
        LOGGER.info("user:{}", sellMember);
        SessionVO sessionVO = null;

        if(sellMember != null) {
            sessionVO = new SessionVO();
            sessionVO.setMemberId(sellMember.getSellMemberId());
            sessionVO.setMemberEmail(sellMember.getEmail());
            sessionVO.setMemberName(sellMember.getName());
//            sessionVO.setPassword(member.getPassword());
            sessionVO.setMemberRoles(sellMember.getMemberRoles().stream().map(MemberRole::getRoleId).collect(Collectors.toList()));
        }

        return sessionVO;
    }



}
