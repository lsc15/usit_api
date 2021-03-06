package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;
import lombok.Data;



@Data
@Entity
@Table(name="calculation")
@NamedQuery(name="Calculation.findAll", query="SELECT p FROM Calculation p")
public class Calculation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="calculation_id")
	private Integer calculationId;

	
	@Column(name="type_cd")
	private String typeCd;
	
	@Column(name="status_cd")
	private String statusCd;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="purchase_confirm_date")
	private String purchaseConfirmDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="calculation_due_date")
	private String calculationDueDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="calculation_date")
	private String calculationDate;
	
	@Column(name="amount")
	private int amount;
	
	@Column(name="order_item_id")
	private Integer orderItemId;
	
	@Column(name="sell_member_id")
	private Integer sellMemberId;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SELECT)
//	@JoinColumn(name = "sell_member_id", insertable = false, updatable = false)
//	private SellMember sellMember;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_item_id", insertable = false, updatable = false)
	private UsitOrderItem orderItem;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "sell_member_id", insertable = false, updatable = false)
	private SellMember sellMember;
	
	
	@Column(name="delay_reason")
	private String delayReason;
	
	@Column(name="reg_id")
	private Integer regId;
	
	@Column(name="mod_id")
	private Integer modId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}