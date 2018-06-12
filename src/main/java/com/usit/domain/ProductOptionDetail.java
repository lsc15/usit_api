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
@Table(name="product_option_detail")
public class ProductOptionDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="product_option_detail_id")
	private Integer productOptionDetailId;
	

	@Column(name="product_option_id1")
	private int productOptionId1;
	
	@Column(name = "product_option_id2")
	private int productOptionId2;

	@Column(name = "add_price")
	private int addPrice;

	private int inventory;

	@Column(name = "available_yn")
	private String availableYn;

	@Column(name = "use_yn")
	private String useYn;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "mod_date")
	private LocalDateTime modDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name = "reg_date")
	private LocalDateTime regDate;

	@Column(name = "reg_id")
	private String regId;

	@Column(name = "mod_id")
	private String modId;	
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	
}

