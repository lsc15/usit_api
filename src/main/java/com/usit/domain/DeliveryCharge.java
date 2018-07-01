package com.usit.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the delivery_charge database table.
 * 
 */
@Data
@Entity
@Table(name="delivery_charge")
@NamedQuery(name="DeliveryCharge.findAll", query="SELECT d FROM DeliveryCharge d")
public class DeliveryCharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="add_price")
	private int addPrice;

	private String address;

	private String region;
	
	@Id
	@Column(name="post_code")
	private String postCode;

}