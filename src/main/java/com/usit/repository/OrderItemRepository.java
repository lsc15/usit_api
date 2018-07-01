package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.UsitOrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<UsitOrderItem, Integer>{

    public List<UsitOrderItem> findByOrderId(int orderId);
    
    public List<UsitOrderItem> findByDeliveryStatusCdIn(List<String> deliveryStatusCd);
    
//    public List<UsitOrderItem> findByDeliveryStatusCdInAndTheragenSendYn(List<String> deliveryStatusCd,String theragenSendYn);
    
    public List<UsitOrderItem> findByReturnStatusCd(String returnStatusCd);
    
    public List<UsitOrderItem> findByProductId(int productId);

//    public UsitOrderItem findByKitCode(String kitCode);
    
    public Page<UsitOrderItem> findAllByReturnStatusCdIsNotNull(Pageable page);
    
    public Page<UsitOrderItem> findAllByMemberIdAndReturnStatusCdIsNotNull(Pageable page,Long memberId);

    public List<UsitOrderItem> findByMemberIdAndReturnStatusCd(int memeberId, String returnStatusCd);

    
//    public List<UsitOrderItem> findByInspectionResultUrlNotNull();
    
}