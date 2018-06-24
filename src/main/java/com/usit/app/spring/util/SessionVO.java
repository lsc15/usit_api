package com.usit.app.spring.util;

import java.io.Serializable;
import java.util.List;

public class SessionVO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1397796912746748317L;

    /** 회원ID */
    private long memberId;
    
    /** 회원UID */
    private String memberUid;
    
    /** 회원이메일 */
    private String memberEmail;
    /** 비밀번호 */
    private String password;
    /** 성명 */
    private String memberName;
    /** 권한 */
    private List<String> memberRoles;

    
    public long getMemberId() {
        return memberId;
    }
    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
    
    public String getMemberUid() {
        return memberUid;
    }
    public void setMemberUid(String memberUid) {
        this.memberUid = memberUid;
    }
    public String getMemberEmail() {
        return memberEmail;
    }
    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<String> getMemberRoles() {
        return memberRoles;
    }

    public void setMemberRoles(List<String> memberRoles) {
        this.memberRoles = memberRoles;
    }

}
