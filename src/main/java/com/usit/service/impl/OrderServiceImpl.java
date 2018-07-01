package com.usit.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.domain.DeliveryCharge;
import com.usit.domain.Member;
import com.usit.domain.Product;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderItem;
import com.usit.domain.UsitOrderTransaction;
import com.usit.domain.ProductOption;
import com.usit.repository.CartItemRepository;
import com.usit.repository.DeliveryChargeRepository;
import com.usit.repository.MemberRepository;
import com.usit.repository.OrderItemRepository;
import com.usit.repository.OrderRepository;
import com.usit.repository.ProductOptionRepository;
import com.usit.repository.ProductRepository;
import com.usit.service.OrderService;
import com.usit.util.TimeUtil;

@Service
@Transactional
public class OrderServiceImpl extends CommonHeaderService implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private DeliveryChargeRepository deliveryChargeRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductOptionRepository productOptionRepository;
    
    @Autowired
    private EntityManager em;

    @Override
    public Page<UsitOrder> getUsitOrderListByMemberIdAndUseYn(Long memberId, Pageable pageable) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageable.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageable.getPageSize());

        //결제실패
        String orderStatusCd = "1101";

        Page<UsitOrder> list = orderRepository.findByMemberIdAndOrderStatusCdNotAndUseYn(memberId, pageable,orderStatusCd,"Y");
        logger.info("list.size():{}", list.getSize());

        return list;
    }

    @Override
    public Page<UsitOrder> getUsitOrderListAll(Pageable pageable) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageable.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageable.getPageSize());

        //결제실패
        String orderStatusCd = "1101";

        Page<UsitOrder> list = orderRepository.findAllByOrderStatusCdNot(pageable,orderStatusCd);
        logger.info("list.size():{}", list.getSize());

         return list;
    }

    @Override
    public Page<UsitOrder> getUsitOrderListIn(Pageable pageable,String orderStartDate, String orderEndDate, List<String> deliveryStatusCds) throws Exception{

        logger.debug("pageable.getPageNumber():{}", pageable.getPageNumber());
        logger.debug("pageable.getPageSize():{}", pageable.getPageSize());

        //결제실패
        String orderStatusCd = "1101";

        TypedQuery<Long> countQuery = em.createQuery("" +
                "SELECT count(distinct t1.orderId) " +
                "FROM UsitOrder t1, UsitOrderItem t2 " +
                "WHERE t1.orderId = t2.orderId " +
                "and t1.orderDate between concat(:orderStartDate,' 00:00:00') and concat(:orderEndDate,' 23:59:59')" +
                "and t1.orderStatusCd <> :orderStatusCd " +
                "and t2.deliveryStatusCd in (:deliveryStatusCds) ", Long.class)
                .setParameter("orderStatusCd", orderStatusCd)
                .setParameter("orderStartDate", orderStartDate)
                .setParameter("orderEndDate", orderEndDate)
                .setParameter("deliveryStatusCds", deliveryStatusCds);

        TypedQuery<UsitOrder> pageQuery = em.createQuery("" +
                "SELECT distinct t1 " +
                "FROM UsitOrder t1, UsitOrderItem t2 " +
                "WHERE t1.orderId = t2.orderId " +
                "and t1.orderStatusCd <> :orderStatusCd " +
                "and t1.orderDate between concat(:orderStartDate,' 00:00:00') and concat(:orderEndDate,' 23:59:59')" +
                "and t2.deliveryStatusCd in (:deliveryStatusCds) order by t1.orderId desc ", UsitOrder.class)
                .setParameter("orderStatusCd", orderStatusCd)
                .setParameter("orderStartDate", orderStartDate)
                .setParameter("orderEndDate", orderEndDate)
                .setParameter("deliveryStatusCds", deliveryStatusCds);

        pageQuery.setFirstResult(pageable.getPageNumber());
        pageQuery.setMaxResults(pageable.getPageSize());

        long totCnt = countQuery.getSingleResult();

        List<UsitOrder> resultList = pageQuery.getResultList();
        logger.info("resultList.totCnt():{}", totCnt);
        logger.info("resultList.size():{}", resultList.size());

        Page<UsitOrder> page = new PageImpl<>(resultList, pageable, totCnt);

         return page;
    }
    
    
    
    
    
    
    
    
//    @Override
//    public List<UsitOrder> getUsitOrderExcelListIn(List<Integer> orderItems) throws Exception{
//
//
//        //결제실패
//        String orderStatusCd = "1101";
//
//        TypedQuery<UsitOrder> listQuery = em.createQuery("" +
//                "SELECT distinct t1 " +
//                "FROM My23Order t1, My23OrderItem t2 " +
//                "WHERE t1.orderId = t2.orderId " +
//                "and t1.orderStatusCd <> :orderStatusCd " +
//                "and t2.orderItemId in (:orderItems) order by t1.orderId desc ", UsitOrder.class)
//                .setParameter("orderStatusCd", orderStatusCd)
//                .setParameter("orderItems", orderItems);
//
//        List<UsitOrder> resultList = listQuery.getResultList();
//        logger.info("resultList.size():{}", resultList.size());
//
//
//         return resultList;
//    }
    
    
    

    @Override
    public UsitOrder getUsitOrderByMerchantUid(String merchantUid) throws Exception{

        UsitOrder usitOrder = orderRepository.findFirstByMerchantUid(merchantUid);

        return usitOrder;
    }



    @Override
    public UsitOrder getUsitOrderByOrderId(int orderId) throws Exception{

        UsitOrder usitOrder = orderRepository.findOne(orderId);

        return usitOrder;
    }
    
    
    @Override
    public List<DeliveryCharge> getDeliveryCharge(String postCode) throws Exception{
    	
    	
        List<DeliveryCharge> delivery = deliveryChargeRepository.findAllByPostCode(postCode);
        

        return delivery;
    }



    @Override
    public UsitOrderTransaction saveOrderTransaction(UsitOrderTransaction order) throws Exception{

        UsitOrder pOrder = order.getUsitOrder();
        List<UsitOrderItem> pOrderItems = order.getUsitOrderItems();

        UsitOrderTransaction rsltOrderTransaction = new UsitOrderTransaction();
        UsitOrder rsltOrder = orderRepository.save(pOrder);
        List<UsitOrderItem> rsltOrderItems = new ArrayList<UsitOrderItem>();

        if(rsltOrder.getOrderId() > 0) {
            for(UsitOrderItem orderItem : pOrderItems) {
                orderItem.setOrderId(rsltOrder.getOrderId());
                rsltOrderItems.add(orderItemRepository.save(orderItem));
            }
        }

        rsltOrderTransaction.setUsitOrder(rsltOrder);
        rsltOrderTransaction.setUsitOrderItems(rsltOrderItems);
        return rsltOrderTransaction;
    }

    @Override
    public UsitOrder saveOrder(UsitOrder order) throws Exception{

        UsitOrder rsltOrder = orderRepository.save(order);

        return rsltOrder;
    }



    @Override
    public void saveOrderConfirm(Map<String, String> params) throws Exception{



        if("paid".equals(params.get("status"))) {
//                int addPoint = 0;
            UsitOrder updateOrder = orderRepository.findFirstByMerchantUid(params.get("merchant_uid"));

            updateOrder.setImpUid(params.get("imp_uid"));
            updateOrder.setOrderStatusCd("1102");

            orderRepository.save(updateOrder);



            List<UsitOrderItem> orderItems =  updateOrder.getOrderItems();

            //제고관리
            if(orderItems!=null) {
              int size = orderItems.size();
              for (int i = 0; i < size; i++) {
                      if(orderItems.get(i).getProductOptionId() == null) {
                    	  //상품의 제고수량 갱신
                    	 Product product = productRepository.findOne(orderItems.get(i).getProductId());
                    	 if("Y".equals(product.getInventoryUseYn()) && product.getInventory() != 0 ) {
                    		 product.setInventory(product.getInventory()-orderItems.get(i).getQuantity());	 
                    	 }
                    	 productRepository.save(product);
                      }else {
                    	  //상품옵션의 제고수량 갱신
                    	 ProductOption productOption = productOptionRepository.getOne(orderItems.get(i).getProductOptionId());
                    	 productOption.setInventory(productOption.getInventory() - orderItems.get(i).getQuantity());
                    	 productOptionRepository.save(productOption);
                      }
              }
          }
            
            
            //카트삭제
//            if(orderItems!=null) {
//                int size = orderItems.size();
//                for (int i = 0; i < size; i++) {
//                        if(orderItems.get(i).getCartItemId() == null) {
//                            continue;
//                        }
//                    cartItemRepository.delete(orderItems.get(i).getCartItemId());
//
//                }
//            }




            /*
            //포인트증가
            if(orderItems!=null) {
                int size = orderItems.size();

                for (int i = 0; i < size; i++) {
                	int orderItemPrice;
                	if(orderItems.get(i).getAmount() != 0) {
                		orderItemPrice = orderItems.get(i).getAmount();
                	}else {
                		orderItemPrice = 0;
                	}
                    int quantity = orderItems.get(i).getQuantity();
                    int optionAddPrice = 0;
                        //상품옵션의 가격
                        List<ProductOption> productOption=orderItems.get(i).getProduct().getProductOptions();
                        if(productOption!=null) {
                        for (int j = 0; j < productOption.size(); j++) {
                            optionAddPrice+=productOption.get(j).getAddPrice();
                        }
                        }

                        addPoint += calculatePoint((orderItemPrice+optionAddPrice)*quantity, rate);
                    
                    
                    //쿠폰적용
                    if(orderItems.get(i).getUsedCouponId() != null) {
                    
                    	
                    	Coupon coupon = couponRepository.findOne(orderItems.get(i).getUsedCouponId());
                    	if("Y".equals(coupon.getUseYn()) || !orderItems.get(i).getMemberId().equals(coupon.getMemberId())) {
                    		logger.error("결제 실패 :"+ "사용할 수 없는 쿠폰입니다.");
                            throw new FrameworkException("-1001", "사용할 수 없는 쿠폰입니다."); // 오류 리턴 예시
                    	}
                    	
                    	coupon.setUsedDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
                    	coupon.setUseYn("Y");
                    	couponRepository.save(coupon);
                    }
                    
                    
                }

            }

            if(addPoint != 0) {
            //포인트증감이력 저장
            PointHistory point = new PointHistory();
            point.setAddPoint(addPoint);
            point.setRegId(updateOrder.getMemberId());
            //상품구매코드
            point.setPointTypeCd("1501");
            point.setOrderId(updateOrder.getOrderId());
            point.setMemberId(updateOrder.getMemberId());
            pointHistoryRepository.save(point);
            }
            
            //포인트적용
            Member member = memberRepository.findOne(updateOrder.getMemberId());
            int present = member.getPoint();
            member.setPoint(present+addPoint);
            memberRepository.save(member);
            */
            
            

        }else {
            logger.error("결제 status = paid 가 아닙니다.");

        }



    }




    
    
    
    
/*
    //무료구매시 사용
    @Override
    public void saveOrderSelfConfirm(UsitOrder order) throws Exception{



        if(order.getAmount() == 0) {
                int addPoint = 0;
                UsitOrder updateOrder = orderRepository.findFirstByMerchantUid(order.getMerchantUid());

            updateOrder.setOrderStatusCd("1102");

            orderRepository.save(updateOrder);

            List<UsitOrderItem> orderItems =  updateOrder.getOrderItems();

            //카트삭제
            if(orderItems!=null) {
                int size = orderItems.size();
                for (int i = 0; i < size; i++) {
                        if(orderItems.get(i).getCartItemId() == null) {
                            continue;
                        }
                    cartItemRepository.delete(orderItems.get(i).getCartItemId());

                }
            }

            
            //포인트증가
            if(orderItems!=null) {
                int size = orderItems.size();

                for (int i = 0; i < size; i++) {
                	int orderItemPrice;
                	if(orderItems.get(i).getAmount() != null) {
                		orderItemPrice = orderItems.get(i).getAmount();
                	}else {
                		orderItemPrice = 0;
                	}
                    int quantity = orderItems.get(i).getQuantity();
                    int optionAddPrice = 0;
                        //상품옵션의 가격
                        List<ProductOption> productOption=orderItems.get(i).getProduct().getProductOptions();
                        if(productOption!=null) {
                        for (int j = 0; j < productOption.size(); j++) {
                            optionAddPrice+=productOption.get(j).getAddPrice();
                        }
                        }

                    double rate = orderItems.get(i).getProduct().getPointRate();

                        addPoint += calculatePoint((orderItemPrice+optionAddPrice)*quantity, rate);
                    
                    
                    
                    //쿠폰적용
                    if(orderItems.get(i).getUsedCouponId() != null) {
                    
                    	Coupon coupon = couponRepository.findOne(orderItems.get(i).getUsedCouponId());
                    	if("Y".equals(coupon.getUseYn()) || !orderItems.get(i).getMemberId().equals(coupon.getMemberId())) {
                    		logger.error("결제 실패 :"+ "사용할 수 없는 쿠폰입니다.");
                            throw new FrameworkException("-1001", "사용할 수 없는 쿠폰입니다."); // 오류 리턴 예시
                    	}
                    	
                    	coupon.setUsedDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
                    	coupon.setUseYn("Y");
                    	couponRepository.save(coupon);
                    }
                    
                    
                }

            }

            //포인트증감이력 저장
            if(addPoint != 0) {
            PointHistory point = new PointHistory();
            point.setAddPoint(addPoint);
            point.setRegId(updateOrder.getMemberId());
            //상품구매코드
            point.setPointTypeCd("1501");
            point.setOrderId(updateOrder.getOrderId());
            point.setMemberId(updateOrder.getMemberId());
            pointHistoryRepository.save(point);
            }
            
            //포인트적용
            Member member = memberRepository.findOne(updateOrder.getMemberId());
            int present = member.getPoint();
            member.setPoint(present+addPoint);
            memberRepository.save(member);
           
            
            

        }else {
            logger.error("정상 결제에 대한 처리가 아닙니다.");

        }



    }

*/



    @Override
    public JSONObject updateOrderStatus(UsitOrder order,String returnReasonCd,String returnReasonText) throws Exception{



    	
    	int payment = 0;
    	UsitOrder updateOrder = orderRepository.findOne(order.getOrderId());
            
    	if(updateOrder!=null) {
    		updateOrder.setOrderStatusCd("1104");
    		orderRepository.save(updateOrder);
    		List <UsitOrderItem> updateItems = updateOrder.getOrderItems();
    		List <UsitOrderItem> orderItems = order.getOrderItems();
    		if(updateItems!=null) {
    			for (int i = 0; i < updateItems.size(); i++) {
    				payment += updateItems.get(i).getAmount();
    				
    				
    				UsitOrderItem store = new UsitOrderItem();
    				store=updateItems.get(i);
    				store.setReturnReasonCd(orderItems.get(i).getReturnReasonCd());
    				store.setReturnReasonText(orderItems.get(i).getReturnReasonText());
    				store.setDeliveryStatusCd("1211");
    				store.setReturnReasonCd(returnReasonCd);
    				store.setReturnReasonText(returnReasonText);
                    orderItemRepository.save(store);
                    }
                }


            }

            JSONObject iamPort = new JSONObject();
            if(payment != 0 ) {
            	iamPort= iamPortCancelAPI(updateOrder);
            }else {
            	iamPort.put("type","amount:0");
            }

        if("0".equals(String.valueOf(iamPort.get("code"))) || payment == 0) {

        	/*
            //포인트차감
            int addPoint = 0;
            List <UsitOrderItem> updateItems = updateOrder.getOrderItems();
            if(updateItems!=null) {
                for (Iterator<UsitOrderItem> iterator = updateItems.iterator(); iterator.hasNext();) {
                	UsitOrderItem my23OrderItem = (UsitOrderItem) iterator.next();


                    int optionAddPrice = 0;
                    int quantity = my23OrderItem.getQuantity();
                    int orderItemPrice;
                    if(payment != 0 ) {
                    	orderItemPrice = my23OrderItem.getAmount();
                    }else {
                    	orderItemPrice = 0;
                    }
                   
//                    double rate = my23OrderItem.getProduct().getPointRate();

                    //상품옵션의 가격
                    List<ProductOption> productOption=my23OrderItem.getProduct().getProductOptions();
                    if(productOption!=null) {
                        for (int j = 0; j < productOption.size(); j++) {
                            optionAddPrice+=productOption.get(j).getAddPrice();
                        }
                    }



                        addPoint -= calculatePoint((orderItemPrice+optionAddPrice)*quantity, rate);

                      //쿠폰적용
                        if(my23OrderItem.getUsedCouponId() != null) {
                        	Coupon coupon = couponRepository.findOne(my23OrderItem.getUsedCouponId());
                        	coupon.setUsedDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
                        	coupon.setUseYn("N");
                        	couponRepository.save(coupon);
                        }

                }
            }


            if(addPoint != 0) {
            //포인트증감이력 저장
            PointHistory point = new PointHistory();
            point.setAddPoint(addPoint);
            point.setRegId(updateOrder.getMemberId());
            //상품구매 취소 코드
            point.setPointTypeCd("1502");
            point.setOrderId(updateOrder.getOrderId());
            point.setMemberId(updateOrder.getMemberId());
            pointHistoryRepository.save(point);
            }
            
            //포인트적용
            Member member = memberRepository.findOne(updateOrder.getMemberId());
            int present = member.getPoint();
            member.setPoint(present+addPoint);
            memberRepository.save(member);

            */

            return iamPort;
        }else {

            logger.error("결제취소 실패 :"+ iamPort.get("message"));

            String message = String.valueOf(iamPort.get("message"));
            throw new FrameworkException("-1001", message); // 오류 리턴 예시

        }



    }





    public JSONObject iamPortCancelAPI(UsitOrder order) throws ParseException, org.apache.http.ParseException, IOException {
        String key = "2291864334475827";
        String secret = "uN3RFXiS9e4ZTwmhVQ0sEgcxqkd2DEG8rKZ7HiZRw4QpAdGw3GYIXbfnwsq966hyRhFyzFp6JgvktcRx";

        String url = "https://api.iamport.kr/users/getToken";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "Mozilla/5.0");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("imp_key", key));
        urlParameters.add(new BasicNameValuePair("imp_secret", secret));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        String json = EntityUtils.toString(response.getEntity());

        JSONParser parser = new JSONParser();

        JSONObject jo = (JSONObject) parser.parse(json);
        JSONObject res = (JSONObject) jo.get("response");
        String token = (String) res.get("access_token");

        url = "https://api.iamport.kr/payments/cancel";
        post = new HttpPost(url);
        post.setHeader("Authorization", token);
        post.setHeader("User-Agent", "Mozilla/5.0");
        urlParameters = new ArrayList<NameValuePair>();

        urlParameters.add(new BasicNameValuePair("imp_uid", order.getImpUid()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        response = client.execute(post);
        String resString = EntityUtils.toString(response.getEntity());


        return (JSONObject) parser.parse(resString);

    }




    //포인트계산
    int calculatePoint(int price,double rate) {

        return (int) Math.round(price * (rate / 100));

    }



}
