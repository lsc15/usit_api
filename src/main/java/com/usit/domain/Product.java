package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;



/**
 * The persistent class for the product database table.
 * 
 */
@Data
@Entity
@Table(name="product")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="product_id")
	private Integer productId;

	@Column(name="search_use_yn")
	private String searchUseYn;
	
	@Column(name="category_cd")
	private String categoryCd;

	@Column(name="delivery_price")
	private int deliveryPrice;
	
	@Column(name="delivery_price_cut")
	private int deliveryPriceCut;

	@Column(name="detail_imgs")
	private String detailImgs;
	
	@Column(name="detail_content")
	private String detailContent;
	
	@Column(name="detail_img_use_yn")
	private String detailImgUseYn;
	
	@Column(name="additional_imgs")
	private String additionalImgs;
	
	@Column(name="discount_yn")
	private String discountYn;
	
	@Column(name="discounted_price")
	private int discountedPrice;

	@Column(name="inventory_use_yn")
	private String inventoryUseYn;
	
	private int inventory;

	@Column(name="option_use_yn")
	private String optionUseYn;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Long modId;

	private String description;
	
	private int price;
	
	private String tags;

	@Column(name="sell_member_id")
	private Long sellMemberId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;

	@Column(name="reg_id")
	private Long regId;

	@Column(name="temp_yn")
	private String tempYn;

	private String title;

	@Column(name="title_img")
	private String titleImg;
	
	
	@Column(name="use_yn")
	private String useYn;
	
	@Column(name="delete_yn")
	private String deleteYn;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	@Where(clause = "delete_yn='N'")
	private List<ProductOption> productOptions;
	

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "category_cd", insertable = false, updatable = false)
	private Category category;
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}