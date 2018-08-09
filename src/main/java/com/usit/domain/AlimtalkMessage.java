package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



/**
 * The persistent class for the alimtalk_message database table.
 * 
 */
@Data
@Entity
@Table(name="alimtalk_message")
@NamedQuery(name="AlimtalkMessage.findAll", query="SELECT a FROM AlimtalkMessage a")
public class AlimtalkMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="message_id")
	private Integer messageId;

	private String content;

	@Column(name="mod_id")
	private Integer modId;

	@Column(name="order_id")
	private Integer orderId;
	
	@Column(name="variable_count")
	private int variableCount;
	
	@Column(name="variable_list")
	private String variableList;

	@Column(name="mobile_url")
	private String mobileUrl;
	
	@Column(name="pc_url")
	private String pcUrl;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="reg_id")
	private Integer regId;

	@Column(name="template_cd")
	private String templateCd;

	public AlimtalkMessage() {
	}

}