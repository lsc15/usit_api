package com.usit.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;

@Data
@Entity
@Table(name="posting_history")
public class PostingHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="posting_option_id")
	private Integer postingHistoryId;
	
	private String url;
	
	@Column(name="social_type_cd")
	private String socialTypeCd;
	
	private int sequence;
	
	@Column(name="member_id")
	private String memberId;
	
	@Column(name="product_id")
	private int productId;
	
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
	
	@Column(name="confirm_yn")
	private String confirmYn;
	
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	
}

