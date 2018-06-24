package com.usit.app.spring.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.usit.app.spring.security.extend.wrapper.UserDetailsWrapper;
import com.usit.app.spring.util.SessionVO;

public class SignedMember extends UserDetailsWrapper{

    /**
     *
     */
    private static final long serialVersionUID = 784739378621970725L;

    private final SessionVO vo;
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public SignedMember(SessionVO vo) {
        this.vo = vo;

        Collection<GrantedAuthority> authorities = vo.getMemberRoles().stream().map(roleId->new SimpleGrantedAuthority(roleId)).collect(Collectors.toList());

//        Collection<GrantedAuthority> authorities;
//        List<String> list = vo.getMemberRoles();
//        List<GrantedAuthority> gaList = new ArrayList<GrantedAuthority>();
//
//        for(String s : list) {
//
//            GrantedAuthority ga = new SimpleGrantedAuthority(s);
//            gaList.add(ga);
//
//        }
        setAuthorities(authorities);

    }

    /**
     * 로그인 인증 정보로 활용되는 이메일주소입니다.
     */
    @Override
    public String getUsername() {
        return this.vo.getMemberEmail();
    }

    @Override
    public String getPassword() {
        return this.vo.getPassword();
    }

    
    public long getUserId() {
        return this.vo.getMemberId();
    }

    public void setUserId(long userId) {
        this.vo.setMemberId(userId);
    }
    
    public String getUserUid() {
        return this.vo.getMemberUid();
    }

    public void setUserUid(String userUid) {
        this.vo.setMemberUid(userUid);
    }

    public String getUserEmail() {
        return this.vo.getMemberEmail();
    }

    public SessionVO getMemberInfo() {
        return this.vo;
    }

    public boolean hasRole(String roleId) {
        boolean hasValue = false;
        hasValue = getMemberInfo().getMemberRoles().contains(roleId);
        return hasValue;
    }

    public class ROLE {
        public static final String USIT_USER = "USIT_USER";
        public static final String USIT_PARTNER = "USIT_PARTNER";
        public static final String USIT_ADMIN = "USIT_ADMIN";
    }

}
