package com.usit.app.spring.util;

public class UsitCodeConstants {
    public static final String POSTING_SNS_INSTAGRAM = "2000";
    public static final String USIT_AES256_KEY = "38teamtueseday$$";
    public static final String TEST_ERROR = "FAIL";
    public static final String TYPE_USER = "USER";
    public static final String TYPE_SELLER = "SELLER";
    
    /*주문상태 코드*/
    public static final String ORDER_STATUS_CD_PAYMENT_COMPLETE = "1102";
    public static final String ORDER_STATUS_CD_PAYMENT_CANCEL = "1104";
    /*배송상태 코드*/
    public static final String DELIVERY_STATUS_CD_PAYMENT_COMPLETE = "1201";
    public static final String DELIVERY_STATUS_CD_DELIVERY_STANDBY = "1202";
    public static final String DELIVERY_STATUS_CD_DELIVERY_SEND = "1203";
    public static final String DELIVERY_STATUS_CD_DELIVERY_CACEL = "1211";
    
    public static final String PERIOD_CONDITION_PAYMENT_DATE ="paymentDate";
    public static final String PERIOD_CONDITION_ORDER_CONFIRM_DATE = "orderConfirmDate";
    public static final String PERIOD_CONDITION_SEND_DATE = "sendDate";
    public static final String KEYWORD_CONDITION_ORDERER_NAME = "ordererName";
    public static final String KEYWORD_CONDITION_ORDERER_PHONE = "ordererPhone";
    public static final String KEYWORD_CONDITION_ORDERER_EMAIL = "ordererEmail";
    public static final String KEYWORD_CONDITION_ORDER_ID = "orderId";
    public static final String KEYWORD_CONDITION_ORDER_ITEM_ID = "orderItemId";
    public static final String KEYWORD_CONDITION_PRODUCT_ID = "productId";
    public static final String KEYWORD_CONDITION_TRACKING_NUMBER = "trackingNumber";
    
    
    /* 포인트 적립유형 */
    public static final String POINT_TYPE_CD_PURCHASE ="1501";
    public static final String POINT_TYPE_CD_CANCEL ="1502";
    public static final String POINT_TYPE_CD_INFLUENCER = "1510";

    
    /* 생성자 방지 */
    private UsitCodeConstants(){ 

    }

}