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
@Table(name="unsubscribe")
public class Unsubscribe {

	@Id
	private String email;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

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
