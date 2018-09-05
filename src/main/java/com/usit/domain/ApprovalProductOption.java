package com.usit.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;

@Data
@Entity
@Table(name="approval_product_option")
public class ApprovalProductOption {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="approval_product_option_id")
	private Integer approvalProductOptionId;
	
	@Column(name="approval_product_id")
	private Integer approvalProductId;

	@Column(name="product_option_id")
	private Integer productOptionId;

	private Integer seq;
	
	@Column(name="product_id")
	private Integer productId;
	
	@Column(name = "add_price")
	private int addPrice;

	private int inventory;

	@Column(name = "available_yn")
	private String availableYn;
	
	@Column(name = "delete_yn")
	private String deleteYn;
	
	@Column(name = "use_yn")
	private String useYn;
	
	@Column(name="option_name1")
	private String optionName1;
	
	@Column(name="option_value1")
	private String optionValue1;
	
	@Column(name="option_name2")
	private String optionName2;
	
	@Column(name="option_value2")
	private String optionValue2;
	
	
	@Column(name="approval_status_cd")
	private String approvalStatusCd;
	
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

