package com.usit.app.spring.util;

public class UsitCodeConstants {
	
	public static final String USIT_MASTER_EMAIL_ADDRESS = "eunvanz@gmail.com";
	public static final String USIT_CODE_FRONT_DETAIL_CD = "3200";
	
	public static final String USIT_PRODUCT_URL_PREFIX = "https://usit.co.kr/product/";
	
	public static final String USIT_ENCODE_UTF8 = "UTF-8";
	
    public static final String POSTING_SNS_INSTAGRAM = "2000";
    public static final String USIT_AES256_KEY = "38teamtueseday$$";
    public static final String TEST_SUCCESS = "SUCCESS";
    public static final String TEST_ERROR = "FAIL";
    
    public static final String TYPE_USER = "USER";
    public static final String TYPE_SELLER = "SELLER";
    
    public static final String SEARCH_TYPE_NEW = "new";
    public static final String SEARCH_TYPE_POPULAR = "popular";
    public static final String SEARCH_TYPE_LOWEST = "lowest";
    
    /*코멘트상태 코드*/
    public static final String COMMENT_STATUS_CD_REVIEW = "1001";
    public static final String COMMENT_STATUS_CD_QNA = "1002";
    
    /*주문상태 코드*/
    public static final String ORDER_STATUS_CD_PAYMENT_COMPLETE = "1102";
    public static final String ORDER_STATUS_CD_PAYMENT_CANCEL = "1104";
    
    /*배송상태 코드*/
    public static final String DELIVERY_STATUS_CD_PAYMENT_COMPLETE = "1201";
    public static final String DELIVERY_STATUS_CD_DELIVERY_STANDBY = "1202";
    public static final String DELIVERY_STATUS_CD_DELIVERY_SEND = "1203";
    public static final String DELIVERY_STATUS_CD_DELIVERY_COMPLETE = "1204";
    public static final String DELIVERY_STATUS_CD_DELIVERY_CHANGE_COMPLETE = "1207";
    public static final String DELIVERY_STATUS_CD_DELIVERY_CANCEL = "1211";
    
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
    public static final String PRODUCT_STATUS_CD_APPROVAL = "2206";
    
    /* 포인트 적립유형 */
    public static final String POINT_TYPE_CD_PURCHASE ="1501";
    public static final String POINT_TYPE_CD_CANCEL ="1502";
    
    public static final String POINT_TYPE_CD_SELL ="1505";
    public static final String POINT_TYPE_CD_SELL_CANCEL ="1506";
    
    public static final String POINT_TYPE_CD_WITHDRAW ="1507";
    public static final String POINT_TYPE_CD_WITHDRAW_CANCEL ="1508";
    
    public static final String POINT_TYPE_CD_RECOMMENDER = "1510";
    public static final String POINT_TYPE_CD_EVENT = "1511";
    public static final String POINT_TYPE_CD_RECOMMENDER_CANCEL = "1512";

    
    /*판매자 상태 코드*/
    public static final String SELLMEMBER_TYPE_CD_MASTER = "2302";
    
    
    
    /* 포인트 출금 요청유형 */
    public static final String POINT_WITHDRAW_TYPE_CD_STANDBY ="2501";
    public static final String POINT_WITHDRAW_TYPE_CD_COMPLETE ="2502";
    public static final String POINT_WITHDRAW_TYPE_CD_DENY ="2503";
    
    
    /* 카카오알림톡 요청유형 */
    public static final String KAKAO_WELCOME ="U013";

    /* 정산 코드 유형 */
    public static final String CACULATION_STATUS_CD_STANDBY ="2601";
    public static final String CACULATION_STATUS_CD_COMPLETE ="2602";
    public static final String CACULATION_STATUS_CD_DENY ="2603";
    
    /* 수정심사 요청유형 */
    public static final String APPROVAL_STATUS_CD_APPROVAL ="2701";
    public static final String APPROVAL_STATUS_CD_DENY ="2703";
    public static final String APPROVAL_STATUS_CD_CONFIRM ="2702";
    
    
    /* 기간조건 요청유형 */
    public static final String PERIOD_CONDITION_PURCHASE ="purchaseConfirmDate";
    public static final String PERIOD_CONDITION_DUE_DATE ="calculationDueDate";
    public static final String PERIOD_CONDITION_COMPLETE_DATE ="calculationDate";
    
    /* 정산 유형 */
    public static final String CACULATION_TYEP_CD_PURCHASE ="2801";
    public static final String CACULATION_TYEP_CD_DELIVERY ="2802";
    
    
    /* 네이버 페이 옵션명 */
    public static final String NAVER_PAY_SUPPLEMENTSEARCH = "supplementSearch";
    public static final String NAVER_PAY_OPTIONSEARCH = "optionSearch";
    
    /* 배송완료 후 구매확정일 차감일 */
    public static final int PURCHASE_COMPLETE_DATE = 7;
    
    
    /* 추천인 추가 포인트적립률 */
    
    public static final double FIRST_RECOMMEND_RATE = 2.5;
    
    public static final double SECOND_RECOMMEND_RATE = 1;
    
    public static final double THIRD_RECOMMEND_RATE = 0.5;
    

    
    
    
    /* 생성자 방지 */
    private UsitCodeConstants(){ 

    }

}