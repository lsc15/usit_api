package com.usit.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.app.util.messenger.alimtalk.NpaySender;
import com.usit.app.util.naverpay.NpayOrder;
import com.usit.domain.AlimtalkMessage;
import com.usit.domain.DeliveryCharge;
import com.usit.domain.SellMember;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderItem;
import com.usit.domain.UsitOrderTransaction;
import com.usit.service.CommonService;
//import com.usit.service.OrderService;
import com.usit.service.OrderService;
import com.usit.service.SellMemberService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

/**
 * 주문 관리 컨트롤러
 *
 */
@RestController
@RequiredArgsConstructor
public class UsitOrderController extends CommonHeaderController{

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CommonService commonService;
    
    @Autowired
    private NpaySender npaySender;
    
    @Autowired
    private Environment env;
    
    @Autowired
	SellMemberService sellMemberService;

    /**
     * 나의 주문내역 조회
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/orders/member", method=RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";

        Page<UsitOrder> orderList = null;

        try {

        	int memberId = getSignedMember().getMemberInfo().getMemberId();
            logger.debug("curPage:{}", curPage);
            logger.debug("perPage:{}", perPage);

            Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderId");
            orderList = orderService.getUsitOrderListByMemberIdAndUseYn(memberId, pageRequest);


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
        mav.addObject("data", orderList);

        return mav;
    }

    /**
     * 주문 목록 조회(관리자용)
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
//    @RequestMapping(value="/orders", method=RequestMethod.GET)
//    public ModelAndView listAll(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
//    		@RequestParam("orderStartDate") String orderStartDate, @RequestParam("orderEndDate") String orderEndDate, @RequestParam("deliveryStatusCd") String deliveryStatusCd,@RequestParam("productTypeCd") String productTypeCd) throws Exception{
//
//        ModelAndView mav = new ModelAndView("jsonView");
//
//        Map<String, Object> resultData = new HashMap<String, Object>();
//        String resultCode = "0000";
//        String resultMsg = "";
//
//        Page<My23Order> orderList = null;
//
//        try {
//
//            logger.debug("curPage:{}", curPage);
//            logger.debug("perPage:{}", perPage);
//            logger.debug("deliveryStatusCd:{}", deliveryStatusCd);
//            logger.debug("productTypeCd:{}", productTypeCd);
//
//            List<String> deliveryStatusCds = new ArrayList<String>();
//            List<String> productTypeCds = new ArrayList<String>();
//
//            Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderId");
//
//            StringTokenizer stk = new StringTokenizer(deliveryStatusCd, ",");
//            StringTokenizer stp = new StringTokenizer(productTypeCd, ",");
//            
//            while(stk.hasMoreTokens()) {
//                deliveryStatusCds.add(stk.nextToken());
//            }
//            while(stp.hasMoreTokens()) {
//            	productTypeCds.add(stp.nextToken());
//            }
//            
//            if(deliveryStatusCds.size() > 0) {
//
//
//                orderList = orderService.getMy23OrderListIn(pageRequest, orderStartDate, orderEndDate,deliveryStatusCds);
//            } else {
//                orderList = orderService.getMy23OrderListAll(pageRequest);
//            }
//
//
//            /**
//             * 임시 조회조건 테라젠 연계가 끝나면 삭제한다
//             * 
//             * 
//             */
//            
//            boolean normal = false;
//            boolean gene = false;
//            for (String code : productTypeCds) {
//				if(code.equals("401")) {
//					gene = true;
//				}else if(code.equals("402")) {
//					normal = true;
//				}
//			}
//            
//			int size = orderList.getContent().size();
//			for (int i = 0; i < size; i++) {
//				List<My23OrderItem> items = orderList.getContent().get(i).getOrderItems();
//				int itemSize = items.size();
//				for (int j = 0; j < itemSize; j++) {
//
//					if (!gene && normal) {
//
//						if (items.get(j).getProduct().getProductType().getDetailCd().equals("401")) {
//							items.remove(j);
//							j--;
//							itemSize--;
//						}
//					} else if (gene && !normal) {
//						if (items.get(j).getProduct().getProductType().getDetailCd().equals("402")) {
//							items.remove(j);
//							j--;
//							itemSize--;
//						}
//
//					} else if (!gene && !normal) {
//						if (items.get(j).getProduct().getProductType().getDetailCd().equals("401")
//								|| items.get(j).getProduct().getProductType().getDetailCd().equals("402")) {
//							items.remove(j);
//							j--;
//							itemSize--;
//						}
//					}
//
//				}
//
//			}
//            
//            /**
//             * 끝
//             * */
//            
//            
//            
//            
//            
//            resultData.put("content", orderList.getContent());
//            resultData.put("isFirst", orderList.isFirst());
//            resultData.put("isLast", orderList.isLast());
//            resultData.put("number", orderList.getNumber());
//            resultData.put("totalPages", orderList.getTotalPages());
//            resultData.put("totalCount", orderList.getTotalElements());
//
//        }catch(FrameworkException e){
//            logger.error("CommFrameworkException", e);
//            resultCode = e.getMsgKey();
//            resultMsg = e.getMsg();
//        }catch(Exception e){
//            logger.error("Exception", e);
//            resultCode = "-9999";
//            resultMsg = "처리중 오류가 발생하였습니다.";
//        }
//
//        mav.addObject("result_code", resultCode);
//        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", orderList);
//
//        return mav;
//    }
    
    
    
    
    
    
    
    
    /**
     * 주문 목록 조회(관리자용 엑셀)
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
//    @RequestMapping(value="/orders/excel", method=RequestMethod.GET)
//    public void listAllExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam("orderItems") String orderItems) throws Exception{
//
////        ModelAndView mav = new ModelAndView("jsonView");
//
////        Map<String, Object> resultData = new HashMap<String, Object>();
////        String resultCode = "0000";
////        String resultMsg = "";
//
//        List<My23Order> orderList = null;
//
//        try {
//
//            logger.debug("orderItems:{}", orderItems);
//
//            List<Integer> orderItemIds = new ArrayList<Integer>();
//
//
//            StringTokenizer stk = new StringTokenizer(orderItems, ",");
//            while(stk.hasMoreTokens()) {
//                orderItemIds.add(Integer.parseInt(stk.nextToken()));
//            }
//            if(orderItemIds.size() > 0) {
//                orderList = orderService.getMy23OrderExcelListIn(orderItemIds);
//            } 
//
//            
//            
//          
//			OutputStream out = null;
//			try {
//				response.setContentType("application/vnd.ms-excel;charset=utf-8");
//				response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");
//				WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream());
////				File file = new File("/Users/seungcheol-i/eclipse-workspace/my23-api/test.xls");
////		        WritableWorkbook workbook = Workbook.createWorkbook(file);
//				
//                WritableSheet sheet = workbook.createSheet("Sheet", 0);
//               
//                //서식
//                WritableCellFormat format = new WritableCellFormat();
////                format.setBorder(jxl.format.Border.BOTTOM,jxl.format.BorderLineStyle.THIN);
//                format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
//                format.setAlignment(Alignment.CENTRE);
//                format.setVerticalAlignment(VerticalAlignment.CENTRE);
//                format.setBackground(Colour.AQUA);
//                
//                
//                
//                WritableCellFormat secFormat = new WritableCellFormat();
//                secFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
//                secFormat.setAlignment(Alignment.CENTRE);
//                secFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
//                secFormat.setBackground(Colour.GRAY_25);
//                 
//                
//                sheet.addCell(new Label(0,0,"받는사람",format));
//                sheet.addCell(new Label(1,0,"우편번호",format));
//                sheet.addCell(new Label(2,0,"주소",format));
//                sheet.setColumnView(2, 40);
//                
//                sheet.addCell(new Label(3,0,"연락처1",format));
//                sheet.setColumnView(3, 15);
//                
//                sheet.addCell(new Label(4,0,"연락처2",format));
//                sheet.setColumnView(4, 15);
//                
//                sheet.addCell(new Label(5,0,"수량",format));
//                sheet.addCell(new Label(6,0,"상품코드",format));
//                sheet.addCell(new Label(7,0,"품목명",format));
//                sheet.setColumnView(7, 20);
//                
//                sheet.addCell(new Label(8,0,"배송메시지",format));
//                sheet.setColumnView(8, 25);
//                                
//                sheet.addCell(new Label(9,0,"보내는분성명",secFormat));
//                sheet.setColumnView(9, 15);
//                
//                sheet.addCell(new Label(10,0,"보내는분\n우편번호",secFormat));
//                sheet.setColumnView(10, 25);
//                sheet.addCell(new Label(11,0,"보내는분주소",secFormat));
//                sheet.setColumnView(11, 45);
//                
//                sheet.addCell(new Label(12,0,"보내는분\n전화번호",secFormat));
//                sheet.setColumnView(12, 15);
//                sheet.addCell(new Label(13,0,"발주",secFormat));
//                sheet.addCell(new Label(14,0,"KIT번호",secFormat));
//				
//				
//                int row = 1;
//            
//            for (int i = 0; i < orderList.size(); i++) {
//            
//            List<My23OrderItem> excelOrderItems =	orderList.get(i).getOrderItems();
//            
//            
//            /***
//             * 
//             * 테라젠 임시파일전달을 위한 루프문
//             * 
//             * 
//             */
//					for (int k = 0; k < excelOrderItems.size(); k++) {
//							boolean isFind = false;
//							for (Integer itemId : orderItemIds) {
//								if (itemId.equals(excelOrderItems.get(k).getOrderItemId())) {
//									isFind = true;
//								}
//							}
//							if (!isFind) {
//								excelOrderItems.remove(k);
//								k--;
//							}
//
//					}
//            
//            /**
//             * 
//             * 
//             */
//            
//            
//            
//            
//            
//            
//            
//            int size = 	excelOrderItems.size()+1;
//            
//            for (int j = 1; j < size; j++,row++) {
//				
//            	sheet.addCell(new Label(0,row,orderList.get(i).getReceiverName()));
//            	sheet.addCell(new Label(1,row,orderList.get(i).getReceiverPostcode() ));
//            	sheet.addCell(new Label(2,row,orderList.get(i).getReceiverAddress() + " " + orderList.get(i).getReceiverAddressDetail()));
//            	sheet.addCell(new Label(3,row,orderList.get(i).getReceiverPhone() ));
//            	sheet.addCell(new Label(4,row,""));
//            	sheet.addCell(new Label(5,row,String.valueOf(excelOrderItems.get(j-1).getQuantity()) ));
//            	sheet.addCell(new Label(6,row,String.valueOf(excelOrderItems.get(j-1).getProductId()) ));
//            	sheet.addCell(new Label(7,row,excelOrderItems.get(j-1).getProduct().getTitle() ));
//            	sheet.addCell(new Label(8,row,orderList.get(i).getReceiverMessage() ));
//            	sheet.addCell(new Label(9,row, "마이23헬스케어"));
//            	sheet.addCell(new Label(10,row, ""));
//            	sheet.addCell(new Label(11,row, "서울특별시 용산구 한강대로 366 트윈시티남산오피스 16층"));
//            	sheet.addCell(new Label(12,row, "1855-0023"));
//			}
//            	
//            	
//            	
//            	
//            	
//			}
//             
//            
//            workbook.write();
//            workbook.close();
//            
//            
//			} catch (Exception e) {
//				throw new ServletException("Exception in Excel Servlet", e);
//			} finally {
//				if (out != null)
//					out.close();
//			}
//            
//        }catch(FrameworkException e){
//            logger.error("CommFrameworkException", e);
////            resultCode = e.getMsgKey();
////            resultMsg = e.getMsg();
//        }catch(Exception e){
//            logger.error("Exception", e);
////            resultCode = "-9999";
////            resultMsg = "처리중 오류가 발생하였습니다.";
//        }
//
////        mav.addObject("result_code", resultCode);
////        mav.addObject("result_msg", resultMsg);
////        mav.addObject("data", orderList);
////
////        return mav;
//    }
    
    
    
    

    /**
     * 주문 변경
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
//    @RequestMapping(value="/orders/{orderId}", method=RequestMethod.PUT)
//    public ModelAndView modifyOrder(HttpServletRequest request, @RequestBody My23Order params,@PathVariable int orderId) throws Exception{
//
//        ModelAndView mav = new ModelAndView("jsonView");
//
//        Map<String, Object> resultData = new HashMap<String, Object>();
//        String resultCode = "0000";
//        String resultMsg = "";
//
//        My23Order rsltMy23Order = new My23Order();
//
//        try {
//
//            rsltMy23Order = orderService.saveOrder(params);
//
//        }catch(FrameworkException e){
//            logger.error("CommFrameworkException", e);
//            resultCode = e.getMsgKey();
//            resultMsg = e.getMsg();
//        }catch(Exception e){
//            logger.error("Exception", e);
//            resultCode = "-9999";
//            resultMsg = "처리중 오류가 발생하였습니다.";
//        }
//
//        mav.addObject("result_code", resultCode);
//        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", rsltMy23Order);
//
//        return mav;
//    }


    /**
     * getMerchantUid
     * front 에서 결제가 성공했는지 주기적으로 확인 후 결제완료 페이지노출
     * @param request
     * @param merchantUid
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/orders/merchant-uid/{merchantUid}", method=RequestMethod.GET)
    public ModelAndView getMerchantUid(HttpServletRequest request, @PathVariable("merchantUid") String merchantUid) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";

        try {

            UsitOrder usitOrder = orderService.getUsitOrderByMerchantUid(merchantUid);

            resultData.put("order_status_cd", usitOrder.getOrderStatusCd());
            resultData.put("order_id", usitOrder.getOrderId());

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
        mav.addObject("data", resultData);

        return mav;
    }






    /**
     * 오더ID조회
     *
     * @param request
     * @param merchantUid
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/orders/{orderId}", method=RequestMethod.GET)
    public ModelAndView getOrder(HttpServletRequest request, @PathVariable("orderId") int orderId) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";

        try {
        	
            UsitOrder usitOrder = orderService.getUsitOrderByOrderId(orderId);
            mav.addObject("result_code", resultCode);
            mav.addObject("result_msg", resultMsg);
            mav.addObject("data", usitOrder);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        return mav;
    }

    
    
    
    
    /**
     * 비회원구매 오더ID조회
     *
     * @param request
     * @param merchantUid
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/orders/anonymous/{orderId}", method=RequestMethod.GET)
    public ModelAndView getOrder(HttpServletRequest request, @PathVariable("orderId") Integer orderId,@RequestParam("ordererPhone") String ordererPhone) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";

        try {
        	
            UsitOrder usitOrder = orderService.getUsitOrderByOrderIdAndOrdererPhone(orderId, ordererPhone);
            mav.addObject("result_code", resultCode);
            mav.addObject("result_msg", resultMsg);
            mav.addObject("data", usitOrder);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        return mav;
    }
    

    /**
    * 주문 저장
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/orders", method=RequestMethod.POST)
   public ModelAndView saveOrder(HttpServletRequest request, @RequestBody UsitOrderTransaction params) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Map<String, Object> resultData = new HashMap<String, Object>();
       String resultCode = "0000";
       String resultMsg = "";

       UsitOrderTransaction savedOrderTransaction = new UsitOrderTransaction();

       try {

           logger.debug("{}", params.getUsitOrder().toString());
           logger.debug("{}", params.getUsitOrderItems().toString());
           savedOrderTransaction = orderService.saveOrderTransaction(params);

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
       mav.addObject("data", savedOrderTransaction);

       return mav;
   }
   
   
   
   
   /**
    * 네이버 페이 주문요청
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/orders/npay-request", method=RequestMethod.POST)
   public ModelAndView saveNpayOrder(HttpServletRequest request, @RequestParam("backUrl") String backUrl ,@RequestBody UsitOrderTransaction params) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Map<String, String> resultData = new HashMap<String, String>();
       String resultCode = "0000";
       String resultMsg = "";

       UsitOrderTransaction savedOrderTransaction = new UsitOrderTransaction();

       
       String url = env.getProperty("naver.npay.dev.url");
       String merchantId = env.getProperty("naver.npay.merchant.id");
       String shopId = null;
       String certiKey = env.getProperty("naver.npay.certikey");
       
       try {

//           logger.debug("{}", params.getUsitOrder().toString());
           logger.debug("{}", params.getUsitOrderItems().toString());
          //보류
           savedOrderTransaction = orderService.saveOrderTransaction(params);
           NpayOrder no = new NpayOrder();
           String orderKey = no.sendNpay(url,merchantId,certiKey,backUrl,params);
           String [] result = orderKey.split(":");
           
           if(result[0] != null && UsitCodeConstants.TEST_SUCCESS.equals(result[0])) {
        	   
        	   orderKey = result[1];
        	   shopId = result[2];
           }
           
           resultData.put("certiKey", orderKey);
           resultData.put("shopId", shopId);

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
//       mav.addObject("data", savedOrderTransaction);
       mav.addObject("data", resultData);

       return mav;
   }




   /**
   * 주문 결제 완료 콜백 (이니시스)
   * @param request
   * @param curPage
   * @param perPage
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/orders/confirm", method=RequestMethod.POST)
  public void saveOrderConfirm(HttpServletRequest request, ComUiDTO params) throws Exception{



      ModelAndView mav = new ModelAndView("jsonView");
      Map<String, String> paramData = (Map<String, String>)params.getRequestBodyToObject();
      String resultCode = "0000";
      String resultMsg = "";

      try {


          logger.debug("{}", paramData.get("imp_uid"));
          logger.debug("{}", paramData.get("merchant_uid"));
          logger.debug("{}", paramData.get("status"));


          orderService.saveOrderConfirm(paramData);
          
          if("paid".equals(paramData.get("status"))) {
        	  
        	  UsitOrder order = orderService.getUsitOrderByMerchantUid(paramData.get("merchant_uid"));
        	  
        	  String productName = null;
        	  String company;
        	  int sellerId = 0;
        	  
        	  List<UsitOrderItem> item = order.getOrderItems();
        	  for (Iterator<UsitOrderItem> iterator = item.iterator(); iterator.hasNext();) {
				UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
				sellerId = usitOrderItem.getProduct().getSellMemberId();
				productName = usitOrderItem.getProduct().getTitle();
				
			}
        	  SellMember seller = sellMemberService.getMemberByMemeberId(sellerId);  
        	  
        	  if(item.size() > 1) {
        		  productName = productName+ " 외"+(item.size() - 1)+"건" ;
        	  }
        	  
        	  company = seller.getCompanyNm();
        	  
        	  
        	  String variable [] = new String [3];
        	  variable[0] = order.getOrdererName();
        	  variable[1] = productName;
        	  variable[2] = company;
        	  /**
        	   * 
			   * #{고객명} #{상품명} #{택배사명} #{송장번호}
			   *
			   */
        	  int status = commonService.sendAlimtalk("U014",order.getOrdererPhone(),variable);
        	  logger.info("kakaoStatus : "+status);
        	  
        	  
        	  
        	 
          }
          

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

  }


  
  
  
  /**
  * 주문 결제 완료 최종 요청(Npay)
  * @param request
  * @param curPage
  * @param perPage
  * @return
  * @throws Exception
  */
@Deprecated
@RequestMapping(value="/orders/npay-confirm", method=RequestMethod.POST)
 public void saveOrderNpayConfirm(HttpServletRequest request,@RequestParam("orderId") Integer orderId, @RequestParam("paymentId") String paymentId) throws Exception{



     ModelAndView mav = new ModelAndView("jsonView");
//     Map<String, String> paramData = (Map<String, String>)params.getRequestBodyToObject();
     String resultCode = "0000";
     String resultMsg = "";

     try {


         logger.debug("{ordererPhone}", orderId);
         logger.debug("{paymentId}", paymentId);

         npaySender.setUrl(env.getProperty(""));
         npaySender.setUserId(env.getProperty(""));
         npaySender.setUserPwd(env.getProperty(""));
         
         Map<String, String> paramMap = new HashMap<String, String>();



     	paramMap.put("paymentId", paymentId);

     	JSONObject result = npaySender.send(paramMap);
     	
     	orderService.saveOrderConfirm(result, orderId);
         
     	if("Success".equals(result.get("code"))) {
       	  
       	
     		UsitOrder order = orderService.getUsitOrderByOrderId(orderId);
       	  
       	  
     		String productName = null;
       	  
     		String company;
       	  
     		int sellerId = 0;
       	  
       	  
     		
     		List<UsitOrderItem> item = order.getOrderItems();
       	  
     		for (Iterator<UsitOrderItem> iterator = item.iterator(); iterator.hasNext();) {
     			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
     			sellerId = usitOrderItem.getProduct().getSellMemberId();
     			productName = usitOrderItem.getProduct().getTitle();
     		}

     		SellMember seller = sellMemberService.getMemberByMemeberId(sellerId);  
       	  
     		if(item.size() > 1) {
     			productName = productName+ " 외"+(item.size() - 1)+"건" ;
     		}
       	  
     		company = seller.getCompanyNm();

     		String variable [] = new String [3];
     		variable[0] = order.getOrdererName();
     		variable[1] = productName;
     		variable[2] = company;

     		
     		/**
			   * #{고객명} #{상품명} #{택배사명} #{송장번호}
			   *
			   */
     		int status = commonService.sendAlimtalk("U014",order.getOrdererPhone(),variable);
     		logger.info("kakaoStatus : "+status);
     	}
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
 }
  
  
  
  /**
   * 주문 결제 완료 자체 콜백 (프론트)
   * @param request
   * @param curPage
   * @param perPage
   * @return
   * @throws Exception
   */
  /*
  @RequestMapping(value="/orders/self-confirm", method=RequestMethod.POST)
  public ModelAndView saveOrderSelfConfirm(@RequestBody UsitOrder orderParam) throws Exception{



      ModelAndView mav = new ModelAndView("jsonView");
      String resultCode = "0000";
      String resultMsg = "";

      try {


    	  UsitOrder order = orderService.getUsitOrderByMerchantUid(orderParam.getMerchantUid());
          logger.debug("{}", orderParam.getMerchantUid());


          orderService.saveOrderSelfConfirm(order);
          
          if(order.getPaymentAmount() == 0) {
        	  
        	  
        	  
        	  String variable [] = new String [2];
        	  
  			variable[0] = order.getOrdererName();
  			variable[1] = order.getName();
  			//카카오알림톡 발송
  			int status = commonService.sendAlimtalk("B005",order.getOrdererPhone(),variable);
  			logger.info("kakaoStatus : "+status);
          }
          

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

      return mav;
  }
*/

  /**
   * 주문 취소 완료 콜백 (이니시스)
   * @param request
   * @param curPage
   * @param perPage
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/orders/cancel", method=RequestMethod.POST)
  public ModelAndView cancelOrder(@RequestBody UsitOrder order,@RequestParam("returnReasonCd") String returnReasonCd,@RequestParam("returnReasonText") String returnReasonText) throws Exception{




          ModelAndView mav = new ModelAndView("jsonView");
          String resultCode = "0000";
          String resultMsg = "";


          JSONObject result = null;

          try {

               result = orderService.updateOrderStatus(order,returnReasonCd,returnReasonText);
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
          mav.addObject("data", result);


        return mav;



  }


  
  
  
  
  
  /**
   * 도서산간지역 조회
   *
   * @param request
   * @param merchantUid
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/orders/post-codes/{postCode}", method=RequestMethod.GET)
  public ModelAndView getDeliveryCharge(HttpServletRequest request, @PathVariable("postCode") String postCode) throws Exception{

      ModelAndView mav = new ModelAndView("jsonView");

      String resultCode = "0000";
      String resultMsg = "";

      try {

    	  boolean isCharge = false;
    	  String region ="";
          List<DeliveryCharge> delivery = orderService.getDeliveryCharge(postCode);
          
          if(delivery.size() != 0) {
        	isCharge = delivery.size() > 0;
          	region = delivery.get(0).getRegion();
          }
          
          Map<String, Object> result = new HashMap<String, Object>();
          result.put("result", isCharge);
          result.put("region", region);
          mav.addObject("result_code", resultCode);
          mav.addObject("result_msg", resultMsg);
          mav.addObject("data", result);

      }catch(FrameworkException e){
          logger.error("CommFrameworkException", e);
          resultCode = e.getMsgKey();
          resultMsg = e.getMsg();
      }catch(Exception e){
          logger.error("Exception", e);
          resultCode = "-9999";
          resultMsg = "처리중 오류가 발생하였습니다.";
      }

      return mav;
  }




//카카오 테스트 초 분 시 일 월 주(년)
// 	@Scheduled(cron = "0 21 16 * * ?")
//     public void test() throws Exception{
// 		UsitOrder order = orderService.getUsitOrderByMerchantUid("order_54_1534660882781");
//  	  
//  	  String productName = null;
//  	  String company;
//  	  int sellerId = 0;
//  	  
//  	  List<UsitOrderItem> item = order.getOrderItems();
////  	  for (int i = 0; i < item.size(); i++) {
////  		item.get(i).getProduct().getSellMemberId();
////  		item.get(i).getProduct().getTitle();
////	}
//  	  
//  	  for (Iterator<UsitOrderItem> iterator = item.iterator(); iterator.hasNext();) {
//			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
//			sellerId = usitOrderItem.getProduct().getSellMemberId();
//			productName = usitOrderItem.getProduct().getTitle();
//			
//		}
//  	  SellMember seller = sellMemberService.getMemberByMemeberId(sellerId);  
//  	  
//  	  if(item.size() > 1) {
//  		  productName = productName+ " 외"+(item.size() - 1)+"건" ;
//  	  }
//  	  
//  	  company = seller.getCompanyNm();
//  	  
//  	  
//  	  String variable [] = new String [3];
//  	  variable[0] = order.getOrdererName();
//  	  variable[1] = productName;
//  	  variable[2] = company;
////  	  카카오알림톡 발송 #{고객명},#{상품명},#{판매회사명}
//  	  int status = commonService.sendAlimtalk("U014",order.getOrdererPhone(),variable);
//  	  logger.info("kakaoStatus : "+status);
// 		
//     }

  
  
  



}
