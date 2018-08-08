package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;



/**
 * The persistent class for the point_history database table.
 * 
 */
@Data
@Entity
@Table(name="share_history")
@NamedQuery(name="ShareHistory.findAll", query="SELECT p FROM ShareHistory p")
public class ShareHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="share_id")
	private Integer shareId;

	@Column(name="member_id")
	private Integer memberId;
	
	
	@Column(name="store_key")
	private String storeKey;

	@Column(name="product_id")
	private Integer productId;
	
	private String date;
	
	@Column(name="visit_cnt")
	private int visitCnt;
	
	@Column(name="purchase_cnt")
	private int purchaseCnt;
	
	@Column(name="purchase_amount")
	private int purchaseAmount;
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
	
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