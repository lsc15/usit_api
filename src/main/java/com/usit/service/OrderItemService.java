package com.usit.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.usit.domain.UsitOrderItem;

public interface OrderItemService {

    public UsitOrderItem getUsitOrderItem(int orderId) throws Exception;
    public Page<UsitOrderItem> getUsitOrderItemByMemberIdAndDeliveryStatusCdIn(int memberId, List<String> deliveryStatusCds, Pageable pageable) throws Exception;
    public Map<String,Object> getCountByMemberIdAndDeliveryStatusCdIn(int memberId, List<String> deliveryStatusCds) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdIn(List<String> orderItemStatusCd) throws Exception;
//    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdInAndTheragenSendYn(List<String> orderItemStatusCd,String theragenSendYn) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemByReturnStatusCd(String returnStatusCd) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemListByOrderId(int orderId) throws Exception;
    public Page<UsitOrderItem> getUsitOrderItemListAll(Pageable pageable) throws Exception;
    public Page<UsitOrderItem> getUsitOrderItemReturnRequest(Pageable pageable) throws Exception;
    public Page<UsitOrderItem> getSellerOrderItemList(Pageable pageable,int memberId,String periodCondition,String startDate,String endDate,String keywordCondition,String keyword) throws Exception;
    public Page<UsitOrderItem> findAllByMemberIdAndReturnStatusCdIsNotNull(Pageable pageable,int memberId) throws Exception;
    public UsitOrderItem setOrderItem(UsitOrderItem orderItem) throws Exception;
    public UsitOrderItem setOrderItemTracker(UsitOrderItem orderItem) throws Exception;
    public UsitOrderItem setOrderItemReturnTracker(UsitOrderItem orderItem) throws Exception;
//    public UsitOrderItem setOrderItemSendTheragen(UsitOrderItem orderItem) throws Exception;
    
    public List<UsitOrderItem> getCaculationByDeliveryStatusCd(String DeliveryStatusCd) throws Exception;
    public Map<String,Object> setOrderItemReturnSweetTracker(Map<String, String> paramData) throws Exception;
    public JSONObject updateOrderItemStatus(UsitOrderItem orderItem,String returnReasonCd,String returnReasonText) throws Exception;
    public JSONObject confirmOrderItemCancel(UsitOrderItem orderItem) throws Exception;
    
    
    
    
    
}
