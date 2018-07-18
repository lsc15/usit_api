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
@Table(name="point_history")
@NamedQuery(name="PointHistory.findAll", query="SELECT p FROM PointHistory p")
public class PointHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="point_id")
	private Integer pointId;

	@Column(name="add_point")
	private int addPoint;

	@Column(name="member_id")
	private Integer memberId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Integer modId;

	@Column(name="order_id")
	private Integer orderId;

	@Column(name="point_type_cd")
	private String pointTypeCd;
	
	@Column(name="add_pct")
	private int addPct;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="withdrawable_date")
	private LocalDateTime withdrawableDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;

	@Column(name="reg_id")
	private Integer regId;
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "point_type_cd", insertable = false, updatable = false)
	private UsitCode pointType;

	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}