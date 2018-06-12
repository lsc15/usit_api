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

import lombok.Data;



/**
 * The persistent class for the resource_role database table.
 *
 */

@Data
@Entity
@Table(name="resource_role")
@NamedQuery(name="ResourceRole.findAll", query="SELECT r FROM ResourceRole r")
public class ResourceRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_seq")
    private int roleSeq;

    @Column(name="auth_ncsry_yn")
    private String authNcsryYn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;

    @Column(name="mod_id")
    private String modId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;

    @Column(name="reg_id")
    private String regId;

    @Column(name="req_method")
    private String reqMethod;

    @Column(name="req_uri")
    private String reqUri;

    @Column(name="role_id")
    private String roleId;

    @Column(name="ord_seq")
    private int ordSeq;


    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

}
