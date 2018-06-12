package com.usit.service;

import com.usit.app.spring.util.SessionVO;
import com.usit.domain.Member;

public interface LoginService {

    public SessionVO getLoginMember(String id);

    public SessionVO getLoginShopMember(String id);
    
    public SessionVO getLoginMemberByEmail(String email);
    
    public SessionVO getLoginShopMemberByEmail(String email);

//    public Member setPwdErrCount(String memberId);

}

