package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.usit.util.TimeUtil;

import lombok.Data;

/**
 * The persistent class for the cart_item database table.
 * 
 */
@Data
@Entity
@Table(name="cart_item")
@NamedQuery(name="CartItem.findAll", query="SELECT c FROM CartItem c")
public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cart_item_id")
	private Integer cartItemId;

	@Column(name="member_id")
	private Integer memberId;

	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Integer modId;

	@Column(name="product_id")
	private Integer productId;

	@Column(name="product_option_id", nullable=true)
	private Integer productOptionId;

	private int quantity;

	@Column(name="reg_date")
	private LocalDateTime regDate;

	@Column(name="reg_id")
	private Integer regId;
	
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;
	

	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}