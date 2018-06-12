package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.usit.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.usit.domain.MemberRole;

import lombok.Data;


/**
 * The persistent class for the member database table.
 *
 */
@Data
@Entity
@Table(name="member")
@NamedQuery(name="Member.findAll", query="SELECT m FROM Member m")
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="member_id")
    private String memberId;

    private String email;

    private String mobile;

    private String name;
    
    @Column(name="mod_id")
    private String modId;


    @Column(name="reg_id")
    private String regId;
    
    @Transient
    private String storeKey;
    
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
