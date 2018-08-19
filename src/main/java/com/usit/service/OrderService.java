package com.usit.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.DeliveryCharge;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderTransaction;

public interface OrderService {

    public Page<UsitOrder> getUsitOrderListByMemberIdAndUseYn(int memberId, Pageable pageable) throws Exception;
    public Page<UsitOrder> getUsitOrderListAll(Pageable pageable) throws Exception;
    public Page<UsitOrder> getUsitOrderListIn(Pageable pageable, String orderStartDate, String orderEndDate ,List<String> deliveryStatusCds) throws Exception;
//    public List<UsitOrder> getUsitOrderExcelListIn(List<Integer> orderItems) throws Exception;
    public UsitOrder getUsitOrderByMerchantUid(String merchantUid) throws Exception;
    public UsitOrder getUsitOrderByOrderId(int orderId) throws Exception;
    public UsitOrder getUsitOrderByOrderIdAndOrdererPhone(int orderId,String ordererPhone) throws Exception;
    public List<DeliveryCharge> getDeliveryCharge(String postCode) throws Exception;
    public UsitOrderTransaction saveOrderTransaction(UsitOrderTransaction order) throws Exception;
    public UsitOrder saveOrder(UsitOrder order) throws Exception;
    public void saveOrderConfirm(Map<String, String> paramData) throws Exception;
//    public void saveOrderSelfConfirm(UsitOrder order) throws Exception;
    public JSONObject updateOrderStatus(UsitOrder order,String returnReasonCd,String returnReasonText) throws Exception;


}
