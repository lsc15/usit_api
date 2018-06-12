package com.usit.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.app.spring.util.SessionVO;
import com.usit.domain.Member;
import com.usit.repository.MemberRepository;
import com.usit.service.TestService;

@Service
@Transactional
public class TestServiceImpl extends CommonHeaderService implements TestService {



	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public Member selectUser(Member member) {

		// CommonHeaderService를 상속하면 사용가능.
		SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

		SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴

		logger.debug("sessionVO.getMemberId:{}", sessionVO.getMemberId()); // 로그인한 사용자의 사용자고유 id
		logger.debug("sessionVO.getMemberEmail:{}", sessionVO.getMemberEmail()); // 로그인한 사용자의 이메일주소
		logger.debug("sessionVO.getMemberName:{}", sessionVO.getMemberName()); // 로그인한 사용자의 이름

		Member resultUser = memberRepository.findOne(member.getMemberId());
		return resultUser;
	}

}
