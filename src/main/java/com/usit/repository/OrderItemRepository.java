package com.usit.repository;


import java.time.LocalDateTime;
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
    
    public Page<UsitOrderItem> findByMemberIdAndDeliveryStatusCdIn(Pageable page,int memberId, List<String> deliveryStatusCds);
    
    public List<UsitOrderItem> findByReturnStatusCd(String returnStatusCd);
    
    public List<UsitOrderItem> findByProductId(int productId);

//    public UsitOrderItem findByKitCode(String kitCode);
    
    public Page<UsitOrderItem> findAllByReturnStatusCdIsNotNull(Pageable page);
    
    public Page<UsitOrderItem> findAllByMemberIdAndReturnStatusCdIsNotNull(Pageable page,int memberId);

    public List<UsitOrderItem> findByMemberIdAndReturnStatusCd(int memeberId, String returnStatusCd);

    
    public List<UsitOrderItem> findByDeliveryStatusCdAndDeliveryCompleteDateBetween(String DeliveryStatusCd, LocalDateTime startDate,LocalDateTime endDate);
//    public List<UsitOrderItem> findByInspectionResultUrlNotNull();
    
    public List<UsitOrderItem> findByOrderIdAndDeliveryFeeId(int orderId, int deliveryFeeId);
    
}
