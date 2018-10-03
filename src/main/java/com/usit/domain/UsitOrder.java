package com.usit.domain;

import java.io.Serializable;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.usit.util.TimeUtil;

import lombok.Data;


/**
 * The persistent class for the usit_order database table.
 *
 */
@Data
@Entity
@Table(name="usit_order")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="orderId")
@NamedQuery(name="UsitOrder.findAll", query="SELECT m FROM UsitOrder m")
public class UsitOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="order_id")
    private Integer orderId;

    @Column(name="imp_uid")
    private String impUid;

    @Column(name="member_id")
    private Integer memberId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;

    @Column(name="mod_id")
    private Integer modId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="order_date")
    private LocalDateTime orderDate;

    @Column(name="order_status_cd")
    private String orderStatusCd;

    @Column(name="orderer_email")
    private String ordererEmail;

    @Column(name="orderer_name")
    private String ordererName;

    @Column(name="orderer_phone")
    private String ordererPhone;

    @Column(name="delivery_amount")
    private int deliveryAmount;

    @Column(name="payment_method_cd")
    private String paymentMethodCd;

    @Column(name="receiver_address")
    private String receiverAddress;

    @Column(name="receiver_address_detail")
    private String receiverAddressDetail;

    @Column(name="receiver_message")
    private String receiverMessage;

    @Column(name="receiver_name")
    private String receiverName;

    @Column(name="receiver_phone")
    private String receiverPhone;

    @Column(name="receiver_postcode")
    private String receiverPostcode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;

    @Column(name="reg_id")
    private int regId;

    @Column(name="merchant_uid")
    private String merchantUid;

    @Column(name="use_yn")
    private String useYn;
    
    
    @Column(name="user_ip")
    private String userIp;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "payment_method_cd", insertable = false, updatable = false)
    private UsitCode paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "order_status_cd", insertable = false, updatable = false)
    private UsitCode orderStatus;


//    @OneToMany(fetch = FetchType.EAGER)
//    @Fetch(FetchMode.SELECT)
//    @JoinColumn(name = "order_id", insertable = false, updatable = false)
//    private List<UsitOrderItem> orderItems;
    
    @JsonIgnore
    @OneToMany(mappedBy="order",fetch = FetchType.EAGER)
//    @Fetch(FetchMode.SELECT)
//    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private List<UsitOrderItem> orderItems;
    
//    @OneToMany(mappedBy="order")
//    @JsonIgnore
//    private List<UsitOrderItem> orderItems;




    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
        orderDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

	@Override
	public String toString() {
		return "UsitOrder [orderId=" + orderId + ", impUid=" + impUid + ", memberId=" + memberId + ", modDate="
				+ modDate + ", modId=" + modId + ", orderDate=" + orderDate + ", orderStatusCd=" + orderStatusCd
				+ ", ordererEmail=" + ordererEmail + ", ordererName=" + ordererName + ", ordererPhone=" + ordererPhone
				+ ", deliveryAmount=" + deliveryAmount + ", paymentMethodCd=" + paymentMethodCd + ", receiverAddress="
				+ receiverAddress + ", receiverAddressDetail=" + receiverAddressDetail + ", receiverMessage="
				+ receiverMessage + ", receiverName=" + receiverName + ", receiverPhone=" + receiverPhone
				+ ", receiverPostcode=" + receiverPostcode + ", regDate=" + regDate + ", regId=" + regId
				+ ", merchantUid=" + merchantUid + ", useYn=" + useYn + ",  userIp=" + userIp
				+ ", paymentMethod=" + paymentMethod + ", orderStatus=" + orderStatus + ", orderItems=" + orderItems
				+ "]";
	}
    

}
