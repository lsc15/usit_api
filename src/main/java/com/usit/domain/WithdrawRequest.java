package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;
import lombok.Data;



@Data
@Entity
@Table(name="withdraw_request")
@NamedQuery(name="WithdrawRequest.findAll", query="SELECT p FROM WithdrawRequest p")
public class WithdrawRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="withdraw_request_id")
	private Integer WithdrawRequestId;

	@Column(name="member_id")
	private Integer memberId;
	
	@Column(name="amount")
	private int amount;
	
	@Column(name="bank_cd")
	private String bankCd;
	
	@Column(name="owner_name")
	private String ownerName;
	
	@Column(name="account_number")
	private String accountNumber;
	
	@Column(name="resident_number")
	private String residentNumber;
	
	@Column(name="postcode")
	private String postcode;
	
	@Column(name="address")
	private String address;
	
	@Column(name="address_detail")
	private String addressDetail;
	
	@Column(name="status_cd")
	private String statusCd;
	
	@Column(name="deny_reason")
	private String denyReason;
	
	@Column(name="reg_id")
	private Integer regId;
	
	@Column(name="mod_id")
	private Integer modId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}