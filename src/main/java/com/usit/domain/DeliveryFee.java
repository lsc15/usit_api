package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;

/**
 * The persistent class for the cart_item database table.
 * 
 */
@Data
@Entity
@Table(name="delivery_fee")
@NamedQuery(name="DeliveryFee.findAll", query="SELECT c FROM DeliveryFee c")
public class DeliveryFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="delivery_fee_id")
	private Integer deliveryFeeId;

	@Column(name="free_condition")
	private Integer freeCondition;
	
	@Column(name="amount")
	private Integer amount;
	
	
	@Column(name="order_item_total_amount")
	private Integer orderItemTotalAmount;
	
	@Column(name="sell_member_id")
	private Integer sellMemberId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Integer modId;


	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;

	@Column(name="reg_id")
	private Integer regId;
	

	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}