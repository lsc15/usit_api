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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.usit.util.TimeUtil;

import lombok.Data;


/**
 * The persistent class for the usit_order_item database table.
 *
 */
@Data
@Entity
@Table(name="usit_order_item")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="orderItemId")//entity간 조인시 json단에서 무한재귀현상을 막아주는 어노테이션
@NamedQuery(name="UsitOrderItem.findAll", query="SELECT m FROM UsitOrderItem m")
public class UsitOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="order_item_id")
    private Integer orderItemId;

    @Column(name="member_id")
    private Integer memberId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="change_complete_date")
    private LocalDateTime changeCompleteDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="change_request_date")
    private LocalDateTime changeRequestDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;

    @Column(name="mod_id")
    private Integer modId;

    @Column(name="order_id")
    private Integer orderId;

    @Column(name="product_id")
    private Integer productId;

    @Column(name="product_option_id")
    private Integer productOptionId;

    private int amount;
    
    private int quantity;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;

    @Column(name="reg_id")
    private Integer regId;

    @Column(name="delivery_status_cd")
    private String deliveryStatusCd;

    @Column(name="tracking_number")
    private String trackingNumber;

    @Column(name="cart_item_id")
    private Integer cartItemId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="payment_date")
    private LocalDateTime paymentDate;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="order_confirm_date")
    private LocalDateTime orderConfirmDate;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="send_date")
    private LocalDateTime sendDate;
    
    @Column(name="sell_member_id")
    private Integer sellMemberId;
    
    @Column(name="store_key")
    private String storeKey;
    
    
    @Column(name="return_object_type_cd")
    private String returnObjectTypeCd;
    
    @Column(name="return_receiver_name")
    private String returnReceiverName;
    
    @Column(name="return_receiver_phone")
    private String returnReceiverPhone;
    
    @Column(name="return_receiver_postcode")
    private String returnReceiverPostcode;
    
    @Column(name="return_receiver_address")
    private String returnReceiverAddress;
    
    @Column(name="return_receiver_address_detail")
    private String returnReceiverAddressDetail;
    
    @Column(name="return_receiver_message")
    private String returnReceiverMessage;
    
    @Column(name="return_status_cd")
    private String returnStatusCd;
    
    @Column(name="return_tracking_number")
    private String returnTrackingNumber;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="return_reg_date")
    private LocalDateTime returnRegDate;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="return_mod_date")
    private LocalDateTime returnModDate;
    
    
    @Column(name="return_reason_cd")
    private String returnReasonCd;
    
    
    @Column(name="return_reason_text")
    private String returnReasonText;
    
    @Column(name="st_res_cd")
    private String stResCd;
    
    
    @Column(name="st_res_nm")
    private String stResNm;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "delivery_status_cd", insertable = false, updatable = false)
    private UsitCode deliveryStatus;


    
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "return_object_type_cd", insertable = false, updatable = false)
    private UsitCode returnObjectType;
    
    
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "return_status_cd", insertable = false, updatable = false)
    private UsitCode returnStatus;
    
    @ManyToOne(fetch = FetchType.EAGER)
    //@Fetch(FetchMode.SELECT)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private UsitOrder order;
    
	public UsitOrderItem() {
    }
    

    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

}
