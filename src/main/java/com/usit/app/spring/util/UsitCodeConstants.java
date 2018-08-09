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
    
    public static final String PERIOD_CONDITION_REQUEST_DATE ="regDate";
    public static final String PERIOD_CONDITION_PROCESS_DATE = "modDate";
    public static final String KEYWORD_CONDITION_OWNER_NAME = "ownerName";
    public static final String KEYWORD_CONDITION_ACCOUNT_NUMBER = "accountNumber";
    
    
    
    
    /* 상품상태 코드 */
    public static final String PRODUCT_STATUS_CD_TEMP = "2201";
    public static final String PRODUCT_STATUS_CD_DELETE = "2204";
    public static final String PRODUCT_STATUS_CD_ENROLL = "2203";
    
    /* 포인트 적립유형 */
    public static final String POINT_TYPE_CD_PURCHASE ="1501";
    public static final String POINT_TYPE_CD_CANCEL ="1502";
    
    public static final String POINT_TYPE_CD_SELL ="1505";
    public static final String POINT_TYPE_CD_SELL_CANCEL ="1506";
    
    public static final String POINT_TYPE_CD_WITHDRAW ="1507";
    public static final String POINT_TYPE_CD_WITHDRAW_CANCEL ="1508";
    
    public static final String POINT_TYPE_CD_INFLUENCER = "1510";

    
    /*판매자 상태 코드*/
    public static final String SELLMEMBER_TYPE_CD_MASTER = "2302";
    
    
    
    /* 포인트 출금 요청유형 */
    public static final String POINT_WITHDRAW_TYPE_CD_STANDBY ="2501";
    public static final String POINT_WITHDRAW_TYPE_CD_COMPLETE ="2502";
    public static final String POINT_WITHDRAW_TYPE_CD_DENY ="2503";
    
    
    /* 카카오알림톡 요청유형 */
    public static final String KAKAO_WELCOME ="U001";
    
    
    /* 생성자 방지 */
    private UsitCodeConstants(){ 

    }

}