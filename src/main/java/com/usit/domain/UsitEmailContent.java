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

@Data
@Entity
@Table(name="usit_email_content")
@NamedQuery(name="UsitEmailContent.findAll", query="SELECT m FROM UsitEmailContent m")
public class UsitEmailContent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="usit_email_content_id")
	private Integer usitEmailContentId;
	
	private String content;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Integer modId;
	
	@Column(name="reg_id")
	private Integer regId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
}
