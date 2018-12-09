package com.usit.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.DateUtil;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.domain.DeliveryFee;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.domain.Product;
import com.usit.domain.ProductOption;
import com.usit.domain.UsitOrderItem;
import com.usit.domain.SellMember;
import com.usit.domain.ShareHistory;
import com.usit.domain.UsitOrder;
import com.usit.repository.DeliveryFeeRepository;
import com.usit.repository.MemberRepository;
import com.usit.repository.OrderItemRepository;
import com.usit.repository.OrderRepository;
import com.usit.repository.PointHistoryRepository;
import com.usit.repository.ProductOptionRepository;
import com.usit.repository.ProductRepository;
import com.usit.repository.SellMemberRepository;
import com.usit.repository.ShareHistoryRepository;
import com.usit.service.OrderItemService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

//@Service
@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl extends CommonHeaderService implements OrderItemService {


	
	@Autowired
    private OrderRepository orderRepository;
	
    @Autowired
    private OrderItemRepository orderItemRepository;
	
	@Autowired
	private SellMemberRepository sellMemberRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
	private ProductOptionRepository productOptionRepository;
	
	@Autowired
    private PointHistoryRepository pointHistoryRepository;
    
    @Autowired
    private ShareHistoryRepository shareHistoryRepository;
    
    @Autowired
    private DeliveryFeeRepository deliveryFeeRepository;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	EntityManager entityManager;

	
	
    @Override
    public UsitOrderItem getUsitOrderItem(int orderItemId) throws Exception {
    	UsitOrderItem usitOrderItem = orderItemRepository.findOne(orderItemId);
        return usitOrderItem;
    }

    
    
    @Override
    public Page<UsitOrderItem> getUsitOrderItemByMemberIdAndDeliveryStatusCdIn(int memberId,List<String> deliveryStatusCds, Pageable pageRequest) throws Exception {
    	Page<UsitOrderItem> list = orderItemRepository.findByMemberIdAndDeliveryStatusCdIn(pageRequest,memberId,deliveryStatusCds);
        return list;
    }
    
    
    
    
    @Override
    public Map<String,Object> getCountByMemberIdAndDeliveryStatusCdIn(int memberId,List<String> deliveryStatusCds) throws Exception{


    	Map<String,Object> response = new HashMap<String, Object>();

    	
        Query countQuery = entityManager.createQuery("" +
                "SELECT deliveryStatusCd as state, count(deliveryStatusCd) as cnt " +
                "FROM UsitOrderItem " +
                "WHERE memberId = :memberId " +
                "and deliveryStatusCd in (:deliveryStatusCds) " +
                "group by deliveryStatusCd ")
                .setParameter("memberId", memberId)
                .setParameter("deliveryStatusCds", deliveryStatusCds);

        List <?> list = countQuery.getResultList();

        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            Object[] n = (Object[]) it.next();
            response.put((String)n[0], (Long)n[1]);
        }
        
        

         return response;
    }
    
    
    
    
    
    @Override
    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdIn(List<String> orderItemStatusCd) throws Exception {
    	List<UsitOrderItem> list = orderItemRepository.findByDeliveryStatusCdIn(orderItemStatusCd);
        return list;
    }

//    @Override
//    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdInAndTheragenSendYn(List<String> orderItemStatusCd,String theragenSendYn) throws Exception {
//    	List<UsitOrderItem> list = orderItemRepository.findByDeliveryStatusCdInAndTheragenSendYn(orderItemStatusCd,theragenSendYn);
//        return list;
//    }
    
    
    @Override
    public List<UsitOrderItem> getUsitOrderItemByReturnStatusCd(String returnStatusCd) throws Exception {
    	List<UsitOrderItem> list = orderItemRepository.findByReturnStatusCd(returnStatusCd);
        return list;
    }
    
    
    @Override
    public List<UsitOrderItem> getUsitOrderItemListByOrderId(int orderId) throws Exception {
        List<UsitOrderItem> list = orderItemRepository.findByOrderId(orderId);
        return list;
    }

    @Override
    public Page<UsitOrderItem> getUsitOrderItemListAll(Pageable pageable) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageable.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageable.getPageSize());
        Page<UsitOrderItem> list = orderItemRepository.findAll(pageable);
        logger.info("list.size():{}", list.getSize());
        return list;
    }
    
    
    @Override
    public List<UsitOrderItem> getCaculationByDeliveryStatusCd(String deliveryStatusCd) throws Exception {
		int year = Integer.parseInt(DateUtil.getCurrYyyy());
		int month = Integer.parseInt(DateUtil.getCurrMm());
		int day = Integer.parseInt(DateUtil.getCurrDd());
		LocalDateTime s = LocalDateTime.of(year, month, day, 00, 00, 00);
		s = s.minusDays(UsitCodeConstants.PURCHASE_COMPLETE_DATE);
		LocalDateTime e = LocalDateTime.of(year, month, day, 23, 59, 59);
		e = e.minusDays(UsitCodeConstants.PURCHASE_COMPLETE_DATE);
    	List<UsitOrderItem> list = orderItemRepository.findByDeliveryStatusCdAndDeliveryCompleteDateBetween(deliveryStatusCd,s,e);
        return list;
    }
    
    
    
    
    @Override
    public Page<UsitOrderItem> getUsitOrderItemReturnRequest(Pageable pageRequest) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageRequest.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageRequest.getPageSize());
        Page<UsitOrderItem> list = orderItemRepository.findAllByReturnStatusCdIsNotNull(pageRequest);
        logger.info("list.size():{}", list.getSize());
        return list;
    }
    
    
    
    @Override
    public Page<UsitOrderItem> getSellerOrderItemList(Pageable pageRequest,int memberId,String periodCondition,String startDate,String endDate,String keywordCondition,String keyword) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageRequest.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageRequest.getPageSize());
        
        
        SellMember sellMember = sellMemberRepository.getOne(memberId);
        
        
     // CriteriaBuilder 인스턴스를 작성한다.

    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    	// CriteriaQuery 인스턴스를 생성한다. Board 제네릭 형식으로...

    	CriteriaQuery<UsitOrderItem> criteriaQuery = criteriaBuilder.createQuery(UsitOrderItem.class);



    	// Root는 영속적 엔티티를 표시하는 쿼리 표현식이다. SQL의 FROM 과 유사함

    	Root<UsitOrderItem> root = criteriaQuery.from(UsitOrderItem.class);

    	List<Predicate> restrictions = new ArrayList<>();

    	// SQL의 WHERE절이다. 조건부는 CriteriaBuilder에 의해 생성

    	// 리스트로 작성
    	Predicate expFromDate = null;
    	Predicate expToDate = null;
    	Predicate expSellerId = null;
    	Predicate expKeyword = null;
    	Predicate and = null;
    	
    
    	//기간 조건확인
    	if(periodCondition.equals(UsitCodeConstants.PERIOD_CONDITION_ORDER_CONFIRM_DATE)) {
    		expFromDate = criteriaBuilder.greaterThanOrEqualTo(root.get("orderConfirmDate"), startDate);
    		expToDate = criteriaBuilder.lessThanOrEqualTo(root.get("orderConfirmDate"), endDate);
    	}else if(periodCondition.equals(UsitCodeConstants.PERIOD_CONDITION_PAYMENT_DATE)) {
    		expFromDate = criteriaBuilder.greaterThanOrEqualTo(root.get("paymentDate"), TimeUtil.getStringToDateTime(startDate+"0000"));
    		expToDate = criteriaBuilder.lessThanOrEqualTo(root.get("paymentDate"), TimeUtil.getStringToDateTime(endDate+"2359"));
    	}else if(periodCondition.equals(UsitCodeConstants.PERIOD_CONDITION_SEND_DATE)) {
    		expFromDate = criteriaBuilder.greaterThanOrEqualTo(root.get("sendDate"), startDate);
    		expToDate = criteriaBuilder.lessThanOrEqualTo(root.get("sendDate"), endDate);
    	}
    	
    	//판매자 아이디조건
    	expSellerId = criteriaBuilder.equal(root.get("sellMemberId"), memberId);
    	
    	//키워드 조건확인
    	if(keywordCondition != null && !keywordCondition.equals("")) {
    		
//    		Join<UsitOrderItem, UsitOrder> join = root.join("orderId");
    		Join<UsitOrderItem, UsitOrder> join = root.join("order");
    		
    		if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ORDER_ID)) {
    	
    			expKeyword = criteriaBuilder.equal(root.get("orderId"), keyword);
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ORDER_ITEM_ID)) {
    			expKeyword = criteriaBuilder.equal(root.get("orderItemId"), keyword);	
    	
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ORDERER_EMAIL)) {
    			expKeyword = criteriaBuilder.equal(join.get( "ordererEmail"),keyword);
    	
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ORDERER_NAME)) {
    			expKeyword = criteriaBuilder.equal(join.get( "ordererName"),keyword);
    	
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ORDERER_PHONE)) {
    			expKeyword = criteriaBuilder.equal(join.get( "ordererPhone"),keyword);
    	
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_PRODUCT_ID)) {
    			expKeyword = criteriaBuilder.equal(root.get("productId"), keyword);
    	
    		}else if(keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_TRACKING_NUMBER)) {
    			expKeyword = criteriaBuilder.equal(root.get("trackingNumber"), keyword);
    			
    		}
    		if(UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(sellMember.getMemberTypeCd())) {
    			
    			and = criteriaBuilder.and(expFromDate, expToDate,expKeyword);
    		}else {
    			and = criteriaBuilder.and(expFromDate, expToDate, expSellerId,expKeyword);
    		}
    }else {
    	 
    	if(UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(sellMember.getMemberTypeCd())) {
    	
    		and = criteriaBuilder.and(expFromDate, expToDate);
    	}else {
    		and = criteriaBuilder.and(expFromDate, expToDate, expSellerId);
    	}
    }
    	
    	
    	
    	
    	
    	
    	
    	//Predicate restrictions = criteriaBuilder.equal(root.get("sell_member_id"), memberId);
//    	restrictions.add(criteriaBuilder.equal(root.get("sell_member_id"), memberId));
    	restrictions.add(and);
//    	criteriaQuery.where(restrictions);
    	criteriaQuery.where(restrictions.toArray(new Predicate[]{} ));


    	// ORDER BY절. CriteriaQuery로 생성

//    	criteriaQuery.orderBy(criteriaBuilder.desc(root.get("orderId")));



    	// 뭔가 복잡해 보여도 별거 없다. TypedQuery는 실행 결과를 리턴하는 타입이다.

    	TypedQuery<UsitOrderItem> orderItemListQuery = entityManager.createQuery(criteriaQuery).setFirstResult(pageRequest.getPageNumber()).setMaxResults(pageRequest.getPageSize());

    	List<UsitOrderItem> orderItem = orderItemListQuery.getResultList();
        
        
    	
//    	TypedQuery<UsitOrderItem> query = em.createQuery(cq);
        Page<UsitOrderItem> result = new PageImpl<UsitOrderItem>(orderItem, pageRequest,orderItem.size());
        
        
        
//        Page<UsitOrderItem> list = orderItemRepository.findAllByReturnStatusCdIsNotNull(pageRequest);
        logger.info("list.size():{}", result.getSize());
        return result;
    }    
    
    
    
    @Override
    public Page<UsitOrderItem> findAllByMemberIdAndReturnStatusCdIsNotNull(Pageable pageRequest,int memberId) throws Exception{

        logger.info("pageable.getPageNumber():{}", pageRequest.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageRequest.getPageSize());
        Page<UsitOrderItem> list = orderItemRepository.findAllByMemberIdAndReturnStatusCdIsNotNull(pageRequest,memberId);
        logger.info("list.size():{}", list.getSize());
        return list;
    }
    
    

    @Override
    public UsitOrderItem setOrderItem(UsitOrderItem orderItem) throws Exception{

    	
    	UsitOrderItem asIsOrderItem = orderItemRepository.findOne(orderItem.getOrderItemId());
    	
//    		String asIsDeliveryStatusCd = asIsOrderItem.getDeliveryStatusCd();
//    		String toBeDeliveryStatusCd = orderItem.getDeliveryStatusCd();
    		
    		//시간정보추가
    		orderItem.setRegDate(asIsOrderItem.getRegDate());
    		
    		if(UsitCodeConstants.DELIVERY_STATUS_CD_PAYMENT_COMPLETE.equals(orderItem.getDeliveryStatusCd())) {
    			orderItem.setPaymentDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
    		}else if(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_STANDBY.equals(orderItem.getDeliveryStatusCd())) {
    			orderItem.setOrderConfirmDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
    		}else if(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_SEND.equals(orderItem.getDeliveryStatusCd())) {
    			orderItem.setSendDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
    		}
    		
    		//부분취소 적용
//    		if(!("1211".equals(asIsDeliveryStatusCd)) && "1211".equals(toBeDeliveryStatusCd) ) {
//    			//포인트차감
//                int addPoint = 0;
//                if(orderItem!=null) {
//                        int optionAddPrice = 0;
//                        int quantity = orderItem.getQuantity();
//                        int orderItemPrice;
//                        if(orderItem.getAmount() != 0 ) {
//                        	orderItemPrice = orderItem.getAmount();
//                        }else {
//                        	orderItemPrice = 0;
//                        }
                        
//                        double rate = orderItem.getProduct().getPointRate();
                        //상품옵션의 가격
//                        List<ProductOption> productOption=orderItem.getProduct().getProductOptions();
//                        if(productOption!=null) {
//                        	for (int j = 0; j < productOption.size(); j++) {
//                        		optionAddPrice+=productOption.get(j).getAddPrice();
//                        	}
//                        }
//                            addPoint -= calculatePoint((orderItemPrice+optionAddPrice)*quantity, rate);
                        
                          //쿠폰적용
//                            if(orderItem.getUsedCouponId() != null) {
//                            	Coupon coupon = couponRepository.findOne(orderItem.getUsedCouponId());
//                            	coupon.setUsedDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
//                            	coupon.setUseYn("N");
//                            	couponRepository.save(coupon);
//                            }
                            
                            
//                        if(addPoint != 0) {
//                        //포인트증감이력 저장
//                        PointHistory point = new PointHistory();
//                        point.setAddPoint(addPoint);
//                        point.setRegId(orderItem.getMemberId());
//                        //상품구매 취소 코드
//                        point.setPointTypeCd("1502");
//                        point.setOrderId(orderItem.getOrderId());
//                        point.setMemberId(orderItem.getMemberId());
//                        pointHistoryRepository.save(point);
//                        }
                        
//                        //포인트적용
//                        Member member = memberRepository.findOne(orderItem.getMemberId());
//                        int present = member.getPoint();
//                        member.setPoint(present+addPoint);
//                        memberRepository.save(member);
//                }
//    		}
    		
    		
    		
    		
    		UsitOrderItem my23OrderItem = orderItemRepository.save(orderItem);

        
        
        
        return my23OrderItem;
    }
    		
    	
    //ORM transaction 문제로jdbcTemplagte 사용
    @Transactional(propagation = Propagation.REQUIRED)
    public UsitOrderItem setOrderItemTracker(UsitOrderItem orderItem) throws Exception{

    	
    	UsitOrderItem asIsOrderItem = orderItemRepository.findOne(orderItem.getOrderItemId());
    	
    		
    		
    		
    		asIsOrderItem.setDeliveryStatusCd(orderItem.getDeliveryStatusCd());
//        My23OrderItem my23OrderItem = orderItemRepository.save(asIsOrderItem);

        
        jdbcTemplate.update("UPDATE usit_order_item SET delivery_status_cd = ?, delivery_complete_date = ? WHERE order_item_id = ?", orderItem.getDeliveryStatusCd(),orderItem.getDeliveryCompleteDate() ,orderItem.getOrderItemId());
        
        return asIsOrderItem;
    }
    
    
    
    
  //ORM transation 문제로jdbcTemplagte 사용
    @Transactional(propagation = Propagation.REQUIRED)
    public UsitOrderItem setOrderItemReturnTracker(UsitOrderItem orderItem) throws Exception{

    	
    	UsitOrderItem asIsOrderItem = orderItemRepository.findOne(orderItem.getOrderItemId());
    	
    		
    		
    		asIsOrderItem.setReturnStatusCd(orderItem.getReturnStatusCd());
        jdbcTemplate.update("UPDATE my23_order_item SET return_status_cd = ? WHERE order_item_id = ?", orderItem.getReturnStatusCd(), orderItem.getOrderItemId());
        
        return asIsOrderItem;
    }
    
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> setOrderItemReturnSweetTracker(Map<String, String> paramData) throws Exception{

    
    		
    	UsitOrderItem orderItem = orderItemRepository.findOne(Integer.parseInt(paramData.get("ordCde")));
    	
    		
    		Map<String,Object> response = new HashMap<String, Object>();
    		try {
    		//반품운송장출력
    		if ("Y".equals(paramData.get("trnYn")) &&  "40".equals(String.valueOf(paramData.get("resCde"))) ) {
    			
    			
    	        jdbcTemplate.update("UPDATE my23_order_item SET st_res_cd = ?, st_res_nm = ?, return_tracking_number = ?, mod_date = ?  WHERE order_item_id = ?",paramData.get("resCde") ,paramData.get("resNme"),paramData.get("invoice"),paramData.get("modDate"), orderItem.getOrderItemId());
    	        
    	        //집하완료
    		}else if ("Y".equals(paramData.get("trnYn")) &&  "60".equals(String.valueOf(paramData.get("resCde"))) ) {
    			
    	        jdbcTemplate.update("UPDATE my23_order_item SET st_res_cd = ?, st_res_nm = ?, return_status_cd = ?, mod_date = ?  WHERE order_item_id = ?",paramData.get("resCde") ,paramData.get("resNme"), "1402" ,paramData.get("modDate"),orderItem.getOrderItemId());
    			
    		}else {
    		
    	        jdbcTemplate.update("UPDATE my23_order_item SET st_res_cd = ?, st_res_nm = ?, return_status_cd = ?, mod_date = ?  WHERE order_item_id = ?",paramData.get("resCde") ,paramData.get("resNme"), "1405" ,paramData.get("modDate"),orderItem.getOrderItemId());
    			
    		}
    		response.put("result", "Y");
    		response.put("ordCde", String.valueOf(orderItem.getOrderItemId()));
    		
    		}catch (Exception e) {
				// TODO: handle exception
    			e.printStackTrace();
    			response.put("result", "N");
        		response.put("ordCde", paramData.get("ordCde"));
			}
    		
        return response;
    }
    
    		
    
    
    
    @Override
    public JSONObject confirmOrderItemCancel(UsitOrderItem orderItem) throws Exception{

    	UsitOrderItem updateOrderItem = orderItemRepository.findOne(orderItem.getOrderItemId());
           
    	JSONObject result = new JSONObject();
    	result.put("delivery_price",0);
    	result.put("amount",updateOrderItem.getAmount());

            if(updateOrderItem!=null) {
            	
            	/**
            	 * 1. deliveryfee amount의 값과 관계없이 재설정
            	 * 2. 아이템의 orderItemTotalAmount - orderItem.amount 
            	 * 3. 아이템중 deliveryFeeId로 조회해서 나온 product중 가장 높은 delivery price cut을 deliveryFee free condition에 설정
            	 * 4. amount에는 product delivery price Max값 설정
            	 */
            	//배송비 재정산
            	if(updateOrderItem.getDeliveryFeeId() != null) {
            	
            		int deliveryPrice = 0;
            		DeliveryFee deliveryfee = deliveryFeeRepository.getOne(updateOrderItem.getDeliveryFeeId());
            		int itemTotalAmount = deliveryfee.getOrderItemTotalAmount() - updateOrderItem.getAmount();
//            		deliveryfee.setOrderItemTotalAmount(deliveryfee.getOrderItemTotalAmount() - updateOrderItem.getAmount());
            		
//            		if(deliveryfee.getAmount() == 0) {
            			int MaxDeliveryPrice = 0;
            			int MaxDeliveryPriceCut = 0;
            			
            			List<UsitOrderItem> orderItems = orderItemRepository.findByOrderIdAndDeliveryFeeId(updateOrderItem.getOrderId(), updateOrderItem.getDeliveryFeeId());
            			for (UsitOrderItem usitOrderItem : orderItems) {
            				if(usitOrderItem.getOrderItemId() !=  updateOrderItem.getOrderItemId() && MaxDeliveryPriceCut < usitOrderItem.getProduct().getDeliveryPriceCut() ) {
            					MaxDeliveryPriceCut = usitOrderItem.getProduct().getDeliveryPriceCut(); 
            				}
            				if(usitOrderItem.getOrderItemId() !=  updateOrderItem.getOrderItemId() && MaxDeliveryPrice < usitOrderItem.getProduct().getDeliveryPrice())  {
            					MaxDeliveryPrice = usitOrderItem.getProduct().getDeliveryPrice(); 
            				}
            				
						}
            			if(MaxDeliveryPriceCut <= itemTotalAmount) {
            				MaxDeliveryPrice = 0;
            			}
            			
            			
            			if(itemTotalAmount  == 0) {
            				deliveryPrice += deliveryfee.getAmount();
            			}else {
            				deliveryPrice = deliveryfee.getAmount() - MaxDeliveryPrice;
            			}
            			
            			result.put("delivery_price", deliveryPrice);
            			result.put("delivery_price_cut",MaxDeliveryPriceCut );
//            		}
            	}
            	
            }
            return result;
    }
    
    
    
    
    
    @Override
    public JSONObject updateOrderItemStatus(UsitOrderItem orderItem,String returnReasonCd,String returnReasonText) throws Exception{



    	
    	int payment = 0;
    	UsitOrder updateOrder = orderRepository.findOne(orderItem.getOrderId());
    	
    	UsitOrderItem updateOrderItem = orderItemRepository.findOne(orderItem.getOrderItemId());
            
    	
    	if(updateOrder!=null) {
    		boolean isLast = false;
    		List <UsitOrderItem> updateItems = updateOrder.getOrderItems();
    		for (Iterator iterator = updateItems.iterator(); iterator.hasNext();) {
				UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
				if(orderItem.getOrderItemId() != usitOrderItem.getOrderItemId() && !usitOrderItem.getDeliveryStatusCd().equals(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_CANCEL)) {
					isLast = true;
				}
			}
    		
    		if(isLast) {
    		updateOrder.setOrderStatusCd(UsitCodeConstants.ORDER_STATUS_CD_PAYMENT_CANCEL);
    		orderRepository.save(updateOrder);
    		}
    		
    		
    		if(updateOrderItem!=null) {
    			
    			/**
            	 * 1. deliveryfee amount의 값과 관계없이 재설정
            	 * 2. 아이템의 orderItemTotalAmount - orderItem.amount 
            	 * 3. 아이템중 deliveryFeeId로 조회해서 나온 product중 가장 높은 delivery price cut을 deliveryFee free condition에 설정
            	 * 4. free condition > amount의 경우 amount에는 product delivery price Max값 설정
            	 */
    			int oriAmount = 0;
            	//배송비 재정산
            	if(updateOrderItem.getDeliveryFeeId() != null) {
            	
            		DeliveryFee deliveryfee = deliveryFeeRepository.getOne(updateOrderItem.getDeliveryFeeId());
            		oriAmount = deliveryfee.getAmount();
            		deliveryfee.setOrderItemTotalAmount(deliveryfee.getOrderItemTotalAmount() - updateOrderItem.getAmount());
            		
            			int MaxDeliveryPrice = 0;
            			int MaxDeliveryPriceCut = 0;
            			
            			List<UsitOrderItem> orderItems = orderItemRepository.findByOrderIdAndDeliveryFeeId(updateOrderItem.getOrderId(), updateOrderItem.getDeliveryFeeId());
            			for (UsitOrderItem usitOrderItem : orderItems) {
            				if(usitOrderItem.getOrderItemId() !=  updateOrderItem.getOrderItemId() && MaxDeliveryPriceCut < usitOrderItem.getProduct().getDeliveryPriceCut() ) {
            					MaxDeliveryPriceCut = usitOrderItem.getProduct().getDeliveryPriceCut(); 
            				}
            				if(usitOrderItem.getOrderItemId() !=  updateOrderItem.getOrderItemId() && MaxDeliveryPrice < usitOrderItem.getProduct().getDeliveryPrice())  {
            					MaxDeliveryPrice = usitOrderItem.getProduct().getDeliveryPrice(); 
            				}
            				
						}
            			if(MaxDeliveryPriceCut <= deliveryfee.getOrderItemTotalAmount()) {
            				MaxDeliveryPrice = 0;
            			}
            			
            			deliveryfee.setFreeCondition(MaxDeliveryPriceCut);
            			deliveryfee.setAmount(MaxDeliveryPrice);
            		deliveryFeeRepository.save(deliveryfee);
            	
            	}
    			
    			
    			DeliveryFee deliveryFee = deliveryFeeRepository.getOne(updateOrderItem.getDeliveryFeeId());
    			if(oriAmount - deliveryFee.getAmount() != 0) {
    				payment += oriAmount - deliveryFee.getAmount();
    			}
    			
    			if(deliveryFee.getOrderItemTotalAmount() == 0) {
    				payment += deliveryFee.getAmount();
    			}
    			
//    			payment += updateOrder.getDeliveryAmount();
    			
    			
    			payment += updateOrderItem.getAmount();
    			UsitOrderItem store = new UsitOrderItem();
    			store = updateOrderItem;
    			store.setReturnReasonCd(updateOrderItem.getReturnReasonCd());
    			store.setReturnReasonText(updateOrderItem.getReturnReasonText());
    			store.setDeliveryStatusCd(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_CANCEL);
    			store.setReturnReasonCd(returnReasonCd);
    			store.setReturnReasonText(returnReasonText);
    			orderItemRepository.save(store);
    		}


            
    	}

            JSONObject iamPort = new JSONObject();
            if(payment != 0 ) {
            	logger.info("취소요청 금액: "+ payment);
            	iamPort = iamPortCancelItemAPI(updateOrder,String.valueOf(payment));
            }else {
            	iamPort.put("type","amount:0");
            }

        if("0".equals(String.valueOf(iamPort.get("code"))) || payment == 0) {

        	
            //포인트차감
            int addPoint = 0;
            if(updateOrderItem!=null) {
            	
            	
            	
            	
            	
            	//재고수정 & 판매량 누적 차감
            	if(updateOrderItem.getProductOptionId() == null) {
              	  //상품의 제고수량 갱신 & 판매량누적
              	 Product product = productRepository.findOne(updateOrderItem.getProductId());
              	 if("Y".equals(product.getInventoryUseYn()) && product.getInventory() != 0 ) {
              		 product.setInventory(product.getInventory() + updateOrderItem.getQuantity());	 
              	 }
              	 product.setSoldCnt(product.getSoldCnt() - updateOrderItem.getQuantity());
              	 productRepository.save(product);
                }else {
              	  //상품옵션의 제고수량 갱신 & 판매량누적
              	 ProductOption productOption = productOptionRepository.getOne(updateOrderItem.getProductOptionId());
              	 productOption.setInventory(productOption.getInventory() + updateOrderItem.getQuantity());
              	 productOptionRepository.save(productOption);
              	 
              	 Product product = productRepository.findOne(updateOrderItem.getProductId());
              	 product.setSoldCnt(product.getSoldCnt() - updateOrderItem.getQuantity());
              	 productRepository.save(product);
                }
            	
            	
            	
            	
                
            	//인플루언서 포인트확인
            	if(updateOrderItem.getStoreKey() != null) {
            		//int quantity = usitOrderItem.getQuantity();
            		int orderItemPrice;
            		if(payment != 0 ) {
            			orderItemPrice = updateOrderItem.getAmount();
            		}else {
            			orderItemPrice = 0;
                    }
                   
                    
                    
                  //storeKey 인플루언서 구하기
                    AES256Util aes256Util = null;
            		String uId = null;
            		try {
            			aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
            		} catch (UnsupportedEncodingException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            		
            		try {
            			uId = aes256Util.decrypt(updateOrderItem.getStoreKey());
            		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            		Member influencer = memberRepository.findByMemberUid(uId);
            		Integer memberId = influencer.getMemberId();
            		
            		
            		
//                    double rate = my23OrderItem.getProduct().getPointRate();
                    double productRate = updateOrderItem.getProduct().getCommissionPct();
                    double influencerRate = influencer.getCommissionPct();
                    //상품옵션의 가격 유짓은 orderItem에 옵션값을 포함한다고 이웅희가 2018.07.18에 말함
//                    List<ProductOption> productOption=usitOrderItem.getProduct().getProductOptions();
//                    if(productOption!=null) {
//                        for (int j = 0; j < productOption.size(); j++) {
//                            optionAddPrice+=productOption.get(j).getAddPrice();
//                        }
//                    }

                        
//                    addPoint -= calculatePoint(orderItemPrice * quantity, productRate, influencerRate);
                    addPoint -= calculatePoint(orderItemPrice, productRate, influencerRate);

                      //쿠폰적용
//                        if(usitOrderItem.getUsedCouponId() != null) {
//                        	Coupon coupon = couponRepository.findOne(usitOrderItem.getUsedCouponId());
//                        	coupon.setUsedDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
//                        	coupon.setUseYn("N");
//                        	couponRepository.save(coupon);
//                        }

                        
                        
                        if(addPoint != 0) {
                        	
                        	//포인트적용
                            Member member = memberRepository.findOne(memberId);
                            int present = member.getTotalPoint() + addPoint;
                            member.setTotalPoint(present);
                            memberRepository.save(member);
                            
                            
                            
                            //포인트증감이력 저장
                            PointHistory point = new PointHistory();
                            point.setAddPoint(addPoint);
                            point.setRegId(updateOrder.getMemberId());
                            point.setBalancePoint(present);
                            point.setAddPct((addPoint / orderItemPrice) * 100);
                            
                          //상품구매코드
                            if(updateOrderItem.getStoreKey() != null) {
                            	point.setPointTypeCd(UsitCodeConstants.POINT_TYPE_CD_SELL_CANCEL);	
                            }
                            point.setOrderItemId(updateOrderItem.getOrderItemId());
                            point.setMemberId(memberId);
                            pointHistoryRepository.save(point);
                            
                            
                            
                          
                            
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    		String today = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
                            int productId = updateOrderItem.getProductId();
                    		
                            ShareHistory existShare = shareHistoryRepository.findByDateAndMemberIdAndProductId(today, memberId, productId);
                            
                            
                            
                            
                			try {
                	        if(existShare != null) {
                	    		
                	        	existShare.setPurchaseCnt(existShare.getPurchaseCnt() - 1);
                	        	existShare.setPurchaseAmount(existShare.getPurchaseAmount() - updateOrderItem.getAmount());
                	    		shareHistoryRepository.save(existShare);
                	        
                	        }else{
                	        	ShareHistory share = new ShareHistory();
                	    	    share.setMemberId(memberId);
                	    	    share.setStoreKey(updateOrderItem.getStoreKey());
                	    	    share.setProductId(updateOrderItem.getProductId());
                	    		share.setDate(today);
                	    		share.setPurchaseCnt(-1);
                	    		share.setPurchaseAmount(-updateOrderItem.getAmount());
                	    		shareHistoryRepository.save(share);
                	        }
                			}catch (Exception e) {
                				logger.warn("공유 히스토리 저장 실패.");
                				throw new FrameworkException("-1001", "공유 히스토리 저장에 실패하였습니다."); // 오류 리턴 예시
                			}
                            
                            
                            
                            
                            }
                            
                        
                        
                	}
            }


            

            return iamPort;
        }else {

            logger.error("결제취소 실패 :"+ iamPort.get("message"));

            String message = String.valueOf(iamPort.get("message"));
            throw new FrameworkException("-1001", message); // 오류 리턴 예시

        }



    }





    public JSONObject iamPortCancelItemAPI(UsitOrder order,String payment) throws ParseException, org.apache.http.ParseException, IOException {
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
        urlParameters.add(new BasicNameValuePair("amount", payment));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        response = client.execute(post);
        String resString = EntityUtils.toString(response.getEntity());


        return (JSONObject) parser.parse(resString);

    }
    
    
    
    
    
  //포인트계산 rate는 퍼센트
    int calculatePoint(int price,double rate) {

        return (int) Math.round(price * (rate / 100));

    }
    
  //포인트계산 (상품판매금액 * 수수료 * 인플루언서 수수료) rate는 퍼센트
    int calculatePoint(int price,double rate,double influencerRate) {

        return (int) Math.round((price * (rate / 100)) * (influencerRate / 100));

    }

}
