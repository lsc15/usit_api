package com.usit.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name="ANONYMOUS_ORDER")
public class AnonymousOrder {

	@Id
	@Column(name="anonymous_order_id")
	private String anonymousOrderId;
	
	@Column(name="order_id")
	private String orderId;
	
	private String phone;
	
	@Column(name="user_ip")
	private String userIp;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
}
