package com.usit.app.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.security.util.SecurityUtils;
import com.usit.app.spring.util.SessionVO;

public class CommonHeaderService {

protected final Logger logger = LoggerFactory.getLogger(this.getClass());
private SignedMember  signedMember;

    protected SignedMember getSignedMember() {
//      if (signedUser == null) {
    	signedMember = (SignedMember) SecurityUtils.getSignedUser();
//      }
        return signedMember;
    }

    protected SessionVO getMemberInfo() {
    	SignedMember signedUser = getSignedMember();
        return signedUser.getMemberInfo();
    }

    protected String getPrintUserInfo() {
    	SignedMember signedUser = getSignedMember();
        return signedUser.toString();
    }
}
