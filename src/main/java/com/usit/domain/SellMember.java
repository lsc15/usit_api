package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;


/**
 * The persistent class for the member database table.
 *
 */
@Data
@Entity
@Table(name="sell_member")
@NamedQuery(name="SellMember.findAll", query="SELECT m FROM SellMember m")
public class SellMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="sell_member_id")
    private Integer sellMemberId;
    
    
    @Column(name="sell_member_uid")
    private String sellMemberUid;

    private String name;

    private String mobile;
    
    private String email;
    
    @Column(name="company_nm")
    private String companyNm;
    
    @Column(name="representor_nm")
    private String representorNm;
    
    @Column(name="business_no")
    private String businessNo;
    
    @Column(name="business_address")
    private String businessAddress;
    
    @Column(name="business_address_detail")
    private String businessAddressDetail;
    
    @Column(name="business_postcode")    
    private String businessPostcode;
    
    @Column(name="business_conditions")
    private String businessConditions;
    
    @Column(name="business_type")
    private String businessType;
    
    @Column(name="business_class_cd")
    private String businessClassCd;
    
    @Column(name="online_business_no")
    private String onlineBusinessNo;
    
    @Column(name="represent_category_cd")
    private String representCategoryCd;
    
    @Column(name="release_address")
    private String releaseAddress;
    
    @Column(name="release_address_detail")
    private String releaseAddressDetail;
    
    @Column(name="release_postcode")
    private String releasePostcode;
    
    @Column(name="release_phone")
    private String releasePhone;
    
    @Column(name="return_address")
    private String returnAddress;
    
    @Column(name="return_address_detail")
    private String returnAddressDetail;
    
    @Column(name="return_postcode")
    private String returnPostcode;
    
    @Column(name="return_phone")
    private String returnPhone;
    
    @Column(name="member_type_cd")
    private String memberTypeCd;
    
    
    @Column(name="mod_id")
    private Integer modId;

    @Column(name="reg_id")
    private Integer regId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;
    
    
    @OneToMany(fetch=FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private List<MemberRole> memberRoles;

    
    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }
    



}
