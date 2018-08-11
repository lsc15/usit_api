package com.usit.domain;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
@Table(name="inquiry")
public class Inquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="inquiry_id")
	private Integer inquiryId;
	
	@Column(name="writer_nm")
	private String writerNm;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="company_nm")
	private String companyNm;
	
	@Column(name="content")
	private String content;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime regDate;
	
	@Column(name="state_cd")
	private String stateCd;
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
}
