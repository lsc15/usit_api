package com.usit.controller;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderItem;
import com.usit.service.CommonService;
import com.usit.service.OrderItemService;
import com.usit.service.OrderService;
import com.usit.util.EtcUtil;
import com.usit.util.TimeUtil;
import lombok.RequiredArgsConstructor;

/**
 * 주문 아이템 관리 컨트롤러
 *
 */
@RestController
@RequiredArgsConstructor
public class UsitOrderItemController extends CommonHeaderController{

	
	private static Logger LOGGER = LoggerFactory.getLogger(UsitOrderItemController.class);
	
    @Autowired
    private OrderItemService orderItemService;


    @Autowired
    private CommonService commonService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private Environment env;
    
    
    /**
     * 주문아이템 조회
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/order-items/{orderItemId}", method=RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, @PathVariable String orderItemId) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";

        boolean isAdmin = getSignedMember().hasRole(SignedMember.ROLE.USIT_ADMIN); // 관리자 권한 있나?
        boolean isPartner = getSignedMember().hasRole(SignedMember.ROLE.USIT_PARTNER); // 파트너 권한 있나?
        boolean isUser = getSignedMember().hasRole(SignedMember.ROLE.USIT_USER); // 일반사용자 인가?

        logger.debug("isAdmin:{}", isAdmin );
        logger.debug("isPartner:{}", isPartner );
        logger.debug("isUser:{}", isUser );

        UsitOrderItem orderItem = new UsitOrderItem();
        try {

            int iOrderItemId = Integer.parseInt(orderItemId);

            orderItem = orderItemService.getUsitOrderItem(iOrderItemId);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", orderItem);

        return mav;
    }


    /**
    * 주문 아이템 수정
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   @RequestMapping(value="/order-items/{orderItemId}", method=RequestMethod.PUT)
   public ModelAndView saveOrder(ComUiDTO dto, HttpServletRequest request, @PathVariable String orderItemId, @RequestBody UsitOrderItem paramUsitOrderItem,
			@RequestParam("updateReturnRegDate") String updateReturnRegDate,
			@RequestParam("updateReturnModDate") String updateReturnModDate) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> params = (Map<String, Object>) dto.getRequestBodyToObject();

		Map<String, Object> resultData = new HashMap<String, Object>();
		String resultCode = "0000";
		String resultMsg = "";
		UsitOrderItem result = new UsitOrderItem();
		try {

			
			//스윗트래커 반품접수 불가기간 체크 
			if("1401".equals(paramUsitOrderItem.getReturnStatusCd()) && "1301".equals(paramUsitOrderItem.getReturnObjectTypeCd())) {
				
				
				JSONObject checkResult = commonService.checkSweetTrackerReturnAcceptDay();
					if("Y".equals(checkResult.get("result"))) {

					Map<String, String> returnParams = new HashMap<String, String>();;
					returnParams.put("ordCde", paramUsitOrderItem.getOrderItemId().toString());
					returnParams.put("comCode", "04");
					returnParams.put("invoice",paramUsitOrderItem.getTrackingNumber());
					 //송화인
					returnParams.put("sndNme", paramUsitOrderItem.getReturnReceiverName());
					returnParams.put("sndZip", paramUsitOrderItem.getReturnReceiverPostcode());
					returnParams.put("sndAd1", paramUsitOrderItem.getReturnReceiverAddress());
					returnParams.put("sndAd2", paramUsitOrderItem.getReturnReceiverAddressDetail());
					returnParams.put("sndTel", paramUsitOrderItem.getReturnReceiverPhone());
		            //수화인
					returnParams.put("ownNme", "유짓");
					returnParams.put("ownZip", "15418");
					returnParams.put("ownAd1", "경기도 안산시 단원구 지원로 107");
					returnParams.put("ownAd2", "관리동 1층 109-2호");
					returnParams.put("ownTel", "070-8831-8025");
					returnParams.put("adMemo", paramUsitOrderItem.getReturnReceiverMessage());
					returnParams.put("wipGbn", "3");
					returnParams.put("mode", env.getProperty("running.system"));
					
					
					JSONObject returnResult = commonService.orderSweetTrackerReturn(returnParams);
					
					if("N".equals(returnResult.get("result"))) {
						LOGGER.warn("반품실패.");
						throw new FrameworkException("-1001", "반품접수에 실패하였습니다 관리자에게 문의해주세요."); // 오류 리턴 예시
					}
					
//				               "result":"Y",
//				               "ordCde":"HPLUS1234567890",
//				               "errCde":"",
//				               "errMsg":""
					
				}else {
					LOGGER.warn("반품접수 가능일이 아닙니다.");
					throw new FrameworkException("-1001", "반품접수 가능일이 아닙니다."); // 오류 리턴 예시
				}
			}
			
			
			
			//ORM은 객체가 달라도 DB와 같이 연동된다. 과거값은 변수로 저장할것
			UsitOrderItem pastOrderItem = orderItemService.getUsitOrderItem(paramUsitOrderItem.getOrderItemId());
			String pastTrackingNumber = pastOrderItem.getTrackingNumber();
			
			if (paramUsitOrderItem.getOrderItemId() > 0) {

				if ("true".equals(updateReturnRegDate)) {
					paramUsitOrderItem.setReturnRegDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				} else {
					paramUsitOrderItem.setReturnRegDate(pastOrderItem.getReturnRegDate());
				}
				if ("true".equals(updateReturnModDate)) {
					paramUsitOrderItem.setReturnModDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				} else {
					paramUsitOrderItem.setReturnModDate(pastOrderItem.getReturnRegDate());
				}
			}
			
			if(paramUsitOrderItem.getTrackingNumber() != null) {
				paramUsitOrderItem.setTrackingNumber(paramUsitOrderItem.getTrackingNumber().replaceAll("-", ""));
			}
			
			if(paramUsitOrderItem.getReturnTrackingNumber() != null) {
				paramUsitOrderItem.setReturnTrackingNumber(paramUsitOrderItem.getReturnTrackingNumber().replaceAll("-", ""));
			}

			result = orderItemService.setOrderItem(paramUsitOrderItem);

			// 검진서비스 카카오알림톡 발송
			if ("1203".equals(paramUsitOrderItem.getDeliveryStatusCd()) && pastTrackingNumber == null
					&& paramUsitOrderItem.getTrackingNumber() != null) {

				UsitOrder order = orderService.getUsitOrderByOrderId(paramUsitOrderItem.getOrderId());
				// Member mem = memberService.getMemberByMemeberId(order.getMemberId());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				String departureDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);

				boolean isSendedItem = false;
				int trackingNumberCount = 0;
				String trackingNumber = paramUsitOrderItem.getTrackingNumber();
				List<UsitOrderItem> it = order.getOrderItems();
				for (UsitOrderItem usitOrderItem : it) {
					if(trackingNumber.equals(usitOrderItem.getTrackingNumber())) {
						trackingNumberCount++;
					}
				}
				
				isSendedItem = trackingNumberCount > 1;
				if(!isSendedItem) {
				
				/**
				 * #{고객명} #{주문번호} #{송장번호} #{배송일}
				 */
				String variable[] = new String[4];

				variable[0] = order.getOrdererName();
				variable[1] = String.valueOf(order.getOrderId());
				variable[2] = paramUsitOrderItem.getTrackingNumber();
				variable[3] = departureDate;

				int status = commonService.sendAlimtalk("A009", order.getOrdererPhone(), variable);
				LOGGER.info("kakaoStatus : " + status);
				}
				
				
				// 일반상품 카카오알림톡 발송
			} else if ("1203".equals(paramUsitOrderItem.getDeliveryStatusCd())
					&& pastTrackingNumber == null && paramUsitOrderItem.getTrackingNumber() != null) {
				UsitOrder order = orderService.getUsitOrderByOrderId(paramUsitOrderItem.getOrderId());
				// Member mem = memberService.getMemberByMemeberId(order.getMemberId());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				String departureDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
				
				boolean isSendedItem = false;

				int trackingNumberCount = 0;
				int sameTrackingNumberCount = 0;
				String trackingNumber = paramUsitOrderItem.getTrackingNumber();
				List<UsitOrderItem> it = order.getOrderItems();
				for (UsitOrderItem usitOrderItem : it) {
					
					//주문번호내 송장번호가 이미 있는경우 제품명을 xx외1건 으로 사용하지 않기위한 조건
					//이경우 item에있는 상품명으로 한다.
					if(usitOrderItem.getTrackingNumber() != null) {
						trackingNumberCount++;
					}
					
					if(trackingNumber.equals(usitOrderItem.getTrackingNumber())) {
						sameTrackingNumberCount++;
					}
				}
				isSendedItem = sameTrackingNumberCount > 1;
				if(!isSendedItem) {
					
				/**
				 * #{고객명} #{제품명} #{주문번호} #{택배사} #{송장번호} #{배송일}
				 */

					
				String orderName;
				if(trackingNumberCount > 1) {
					orderName = paramUsitOrderItem.getProduct().getTitle();
				}else {
					orderName = paramUsitOrderItem.getProduct().getTitle();
//					orderName = order.getName();
				}
				
				String variable[] = new String[6];

				variable[0] = order.getOrdererName();
				variable[1] = orderName;
				variable[2] = String.valueOf(order.getOrderId());
				variable[3] = "대한통운";
				variable[4] = paramUsitOrderItem.getTrackingNumber();
				variable[5] = departureDate;

				int status = commonService.sendAlimtalk("B007", order.getOrdererPhone(), variable);
				LOGGER.info("kakaoStatus : " + status);
				}
				
				
			}

		} catch (FrameworkException e) {
			logger.error("CommFrameworkException", e);
			resultCode = e.getMsgKey();
			resultMsg = e.getMsg();
		} catch (Exception e) {
			logger.error("Exception", e);
			resultCode = "-9999";
			resultMsg = e.getCause().getCause().getCause().getCause().getMessage();
		}

		mav.addObject("result_code", resultCode);
		mav.addObject("result_msg", resultMsg);
		mav.addObject("data", result);

		return mav;
	}









   /**
    * 주문아이템 조회
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/return-requests", method=RequestMethod.GET)
   public ModelAndView listReturnRequest(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "returnRegDate");

       Map<String, Object> resultData = new HashMap<String, Object>();
       String resultCode = "0000";
       String resultMsg = "";

       Page<UsitOrderItem> page = null;
       try {

           page = orderItemService.getUsitOrderItemReturnRequest(pageRequest);

       }catch(FrameworkException e){
           logger.error("CommFrameworkException", e);
           resultCode = e.getMsgKey();
           resultMsg = e.getMsg();
       }catch(Exception e){
           logger.error("Exception", e);
           resultCode = "-9999";
           resultMsg = "처리중 오류가 발생하였습니다.";
       }

       mav.addObject("result_code", resultCode);
       mav.addObject("result_msg", resultMsg);
       mav.addObject("data", page);

       return mav;
   }






   /**
    * 주문아이템 조회
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/return-requests/member", method=RequestMethod.GET)
   public ModelAndView listReturnRequestMember(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Map<String, Object> resultData = new HashMap<String, Object>();

       Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderItemId");
       String resultCode = "0000";
       String resultMsg = "";
       Long memberId = getSignedMember().getMemberInfo().getMemberId();
       Page<UsitOrderItem> page = null;
       try {

           page = orderItemService.findAllByMemberIdAndReturnStatusCdIsNotNull(pageRequest, memberId);

       }catch(FrameworkException e){
           logger.error("CommFrameworkException", e);
           resultCode = e.getMsgKey();
           resultMsg = e.getMsg();
       }catch(Exception e){
           logger.error("Exception", e);
           resultCode = "-9999";
           resultMsg = "처리중 오류가 발생하였습니다.";
       }

       mav.addObject("result_code", resultCode);
       mav.addObject("result_msg", resultMsg);
       mav.addObject("data", page);

       return mav;
   }
   
   
   
   
   
   
   
   
   
   /**
    * 반품 접수결과 수신 콜백 (스위트래커)
    * @param request
    * @return
    * @throws Exception
    */
	@RequestMapping(value ="/order-items/return-requests/sweet-tracker", method = RequestMethod.POST)
	public ModelAndView saveOrderConfirm(HttpServletRequest request, ComUiDTO params) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		List <Map<String,Object>> data = new ArrayList<Map<String,Object>>();
//		Map<String, String> paramData = (Map<String, String>) params.getRequestBodyToObject();
		EtcUtil eu = new EtcUtil();
		JSONObject json =  eu.getJsonStringFromMap((Map<String, Object>) params.getRequestBodyToObject());
		

		 JSONParser jsonParser = new JSONParser();
         JSONObject jsonObj = (JSONObject) jsonParser.parse(json.toString());
         JSONArray resultArray = (JSONArray) jsonObj.get("result_data");

         System.out.println("=====Result=====");
         int size = resultArray.size();
         for(int i=0 ; i< size ; i++){
             JSONObject tempObj = (JSONObject) resultArray.get(i);
             Map<String, String> paramData  = eu.getMapFromJsonObject(tempObj);
             paramData.put("modDate",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString());
             // 처리시간
 			logger.debug("{}", paramData.get("trnTim"));
 			// 주문번호
 			logger.debug("{}", paramData.get("ordCde"));
 			// 운송장번호
 			logger.debug("{}", paramData.get("invoice"));
 			// 처리여부 (Y : 처리 / N : 미처리)
 			logger.debug("{}", paramData.get("trnYn"));
 			// 처리상태코드(처리상태코드표 참조)
 			logger.debug("{}", paramData.get("resCde"));
 			// 처리상태명(처리상태코드표 참조)
 			logger.debug("{}", paramData.get("resNme"));
 			// 택배사 코드
 			logger.debug("{}", paramData.get("comCode"));
 			// 스윗트래커 변환 주문번호
 			logger.debug("{}", paramData.get("stOrdCde"));
 			
 			
 			data.add(orderItemService.setOrderItemReturnSweetTracker(paramData));
         }
		

		
		mav.addObject("data", data);
	    
		return mav;

	}
   
   
   
// 택배사 배송상태 동기화 초 분 시 일 월 주(년)
//	@Scheduled(cron = "0 0/10 * * * ?")
//    @Scheduled(cron = "5 * * * * ?")
//   @Scheduled(cron = "0 0 12,17,22 * * ?")
//   @Scheduled(cron = "0 55 9 * * ?")
//   @Scheduled(cron = "0 45 20 * * ?")
//	@Scheduled(cron = "0 0 3,9,13 * * ?")
    public void updateDeliveryStatus() throws Exception{
    	if("real".equals(env.getProperty("running.system"))) {
	   logger.info("@@배송상태동기화 시작");
	   List<String> orderItemStatusCd = new ArrayList<String>();
	   orderItemStatusCd.add("1203");
	   orderItemStatusCd.add("1206");
	   List<UsitOrderItem> checkList = orderItemService.getUsitOrderItemByDeliveryStatusCdIn(orderItemStatusCd);
    	  
    	for (Iterator<UsitOrderItem> iterator = checkList.iterator(); iterator.hasNext();) {
    		UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
			JSONObject result = commonService.checkTracker(usitOrderItem.getTrackingNumber());
			if("Y".equals(result.get("completeYN"))) {
				if("1203".equals(usitOrderItem.getDeliveryStatusCd())){
					logger.info("@@equals 1203: ");
					usitOrderItem.setDeliveryStatusCd("1204");
				}else if("1206".equals(usitOrderItem.getDeliveryStatusCd())){
					logger.info("@@equals 1206: ");
					usitOrderItem.setDeliveryStatusCd("1207");
				}else {
					//추후변경분
				}
				logger.info("@@배송상태동기화 배송번호: "+ usitOrderItem.getTrackingNumber());
				orderItemService.setOrderItemTracker(usitOrderItem);
				logger.info("@@save ");
				logger.info("@@status: " + usitOrderItem.getDeliveryStatusCd());
				Thread.sleep(1000);
			}
		}
    	
    
    	}
    }
    
    
    
    
    
    
    
 // 택배사 반송상태 동기화 초 분 시 일 월 주(년)
// 	@Scheduled(cron = "0 10 3,9,13 * * ?")
     public void updateReturnDeliveryStatus() throws Exception{
     	if("real".equals(env.getProperty("running.system"))) {
     	logger.info("@@반송상태동기화 시작");
     	List<UsitOrderItem> returnCheckList = orderItemService.getUsitOrderItemByReturnStatusCd("1402");
     	  
     	for (Iterator<UsitOrderItem> iterator = returnCheckList.iterator(); iterator.hasNext();) {
 			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
 			JSONObject result = commonService.checkTracker(usitOrderItem.getReturnTrackingNumber());
 			if("Y".equals(result.get("completeYN"))) {
 					usitOrderItem.setReturnStatusCd("1403");
 					
 				logger.info("@@반송상태동기화 배송번호: "+ usitOrderItem.getReturnTrackingNumber());
 				orderItemService.setOrderItemReturnTracker(usitOrderItem);
 				logger.info("@@save ");
 				logger.info("@@status: " +usitOrderItem.getReturnTrackingNumber() +":"+ usitOrderItem.getReturnStatusCd());
 				Thread.sleep(1000);
 			}
 		}
     	
     	}
     }
    
    
     
     
}
    


    
