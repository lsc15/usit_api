package com.usit.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.usit.domain.UsitOrderItem;

public interface OrderItemService {

    public UsitOrderItem getUsitOrderItem(int orderId) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdIn(List<String> orderItemStatusCd) throws Exception;
//    public List<UsitOrderItem> getUsitOrderItemByDeliveryStatusCdInAndTheragenSendYn(List<String> orderItemStatusCd,String theragenSendYn) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemByReturnStatusCd(String returnStatusCd) throws Exception;
    public List<UsitOrderItem> getUsitOrderItemListByOrderId(int orderId) throws Exception;
    public Page<UsitOrderItem> getUsitOrderItemListAll(Pageable pageable) throws Exception;
    public Page<UsitOrderItem> getUsitOrderItemReturnRequest(Pageable pageable) throws Exception;
    public Page<UsitOrderItem> findAllByMemberIdAndReturnStatusCdIsNotNull(Pageable pageable,Long memberId) throws Exception;
    public UsitOrderItem setOrderItem(UsitOrderItem orderItem) throws Exception;
    public UsitOrderItem setOrderItemTracker(UsitOrderItem orderItem) throws Exception;
    public UsitOrderItem setOrderItemReturnTracker(UsitOrderItem orderItem) throws Exception;
//    public UsitOrderItem setOrderItemSendTheragen(UsitOrderItem orderItem) throws Exception;
    public Map<String,Object> setOrderItemReturnSweetTracker(Map<String, String> paramData) throws Exception;
    
    
    
}