package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.UsitOrder;

@Repository
public interface OrderRepository extends JpaRepository<UsitOrder, Integer>{

    public Page<UsitOrder> findByMemberIdAndOrderStatusCdNotAndUseYn(int memberId, Pageable pageable,String orderStatusCd,String useYn);
    public Page<UsitOrder> findByMemberId(int memberId, Pageable pageable);
    public Page<UsitOrder> findByMemberIdAndOrderStatusCdNot(int memberId, String orderStatusCd, Pageable pageable);
    public Page<UsitOrder> findAllByOrderStatusCdNot(Pageable pageable,String orderStatusCd);
    public List<UsitOrder> findAllByMemberId(int memberId);
 
//    public Page<My23Order> findAllByAndDeliverStatusCdInOrderStatusCdNot(Pageable pageable, List<String> deliveryStatusCds, String orderStatusCd);
    public UsitOrder findFirstByMerchantUid(String merchantUid);
    public UsitOrder findByOrderIdAndOrdererPhone(int orderId ,String ordererPhone);

}
