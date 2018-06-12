package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;


/**
 * The persistent class for the auth_role database table.
 *
 */
@Entity
@Table(name="auth_role")
@NamedQuery(name="AuthRole.findAll", query="SELECT a FROM AuthRole a")
public class AuthRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="role_seq")
    private int roleSeq;

    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;

    @Column(name="mod_id")
    private String modId;

    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;

    @Column(name="reg_id")
    private int regId;

    @Column(name="role_desc")
    private String roleDesc;

    @Column(name="role_id")
    private String roleId;

    @Column(name="role_nm")
    private String roleNm;

    @Column(name="role_use_yn", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean roleUseYn;

    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

}
