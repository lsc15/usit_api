package com.usit.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.domain.UsitOrderItem;
import com.usit.repository.MemberRepository;
import com.usit.repository.PointHistoryRepository;
import com.usit.service.PointHistoryService;

@Service
@Transactional
public class PointHistoryServiceImpl extends CommonHeaderService implements PointHistoryService {



	@Autowired
	private PointHistoryRepository pointHistoryRepository;
	
	@Autowired
	private MemberRepository memberRepository;

	
	public PointHistory addPoint(PointHistory point) {
		
		PointHistory returnPoint =  pointHistoryRepository.save(point);
		Member member = memberRepository.findOne(returnPoint.getMemberId());
		member.setTotalPoint(member.getTotalPoint()+returnPoint.getAddPoint());
	    memberRepository.save(member);
		
		return returnPoint;
	}
	
	public Page<PointHistory> getPointListByMemberId(PageRequest page,int memberId){
		/**
		 * 동일한 ID를 가진 객체를 반환하면 프론트에서 객체가 아닌 orderItemId로 받는 문제가있어
		 * 새로운 객체를 생성해서 반환하도록 수정했다. ORM단에서 컨트롤이 가능한지 stackoverflow에 글을 남긴상태 
		 * 
		 * */
		
		Page<PointHistory> list =  pointHistoryRepository.findAllByMemberId(page,memberId);
		List<PointHistory> temp = list.getContent();
		for (int i = 0; i < temp.size(); i++) {
			if(temp.get(i).getOrderItemId() != null) {
			UsitOrderItem item = new UsitOrderItem(); 
			item.setAmount(temp.get(i).getOrderItem().getAmount());
			item.setCartItemId(temp.get(i).getOrderItem().getCartItemId());
			item.setChangeCompleteDate(temp.get(i).getOrderItem().getChangeCompleteDate());
			item.setChangeRequestDate(temp.get(i).getOrderItem().getChangeRequestDate());
			item.setCommissionPct(temp.get(i).getOrderItem().getCommissionPct());
			item.setDeliveryStatus(temp.get(i).getOrderItem().getDeliveryStatus());
			item.setDeliveryStatusCd(temp.get(i).getOrderItem().getDeliveryStatusCd());
			item.setInfCommissionPct(temp.get(i).getOrderItem().getCommissionPct());
			item.setMemberId(temp.get(i).getOrderItem().getMemberId());
			item.setModDate(temp.get(i).getOrderItem().getModDate());
			item.setModId(temp.get(i).getOrderItem().getModId());
			item.setOrder(temp.get(i).getOrderItem().getOrder());
			item.setOrderConfirmDate(temp.get(i).getOrderItem().getOrderConfirmDate());
			item.setOrderId(temp.get(i).getOrderItem().getOrderId());
			item.setOrderItemId(temp.get(i).getOrderItem().getOrderItemId());
			item.setPaymentDate(temp.get(i).getOrderItem().getPaymentDate());
			item.setProduct(temp.get(i).getOrderItem().getProduct());
			item.setProductId(temp.get(i).getOrderItem().getProductId());
			item.setProductOptionId(temp.get(i).getOrderItem().getProductOptionId());
			item.setQuantity(temp.get(i).getOrderItem().getQuantity());
			item.setRegDate(temp.get(i).getOrderItem().getRegDate());
			item.setRegId(temp.get(i).getOrderItem().getRegId());
			item.setReturnModDate(temp.get(i).getOrderItem().getReturnModDate());
			item.setReturnObjectType(temp.get(i).getOrderItem().getReturnObjectType());
			item.setReturnObjectTypeCd(temp.get(i).getOrderItem().getReturnObjectTypeCd());
			item.setReturnReasonCd(temp.get(i).getOrderItem().getReturnReasonCd());
			item.setReturnReasonText(temp.get(i).getOrderItem().getReturnReasonText());
			item.setReturnReceiverAddress(temp.get(i).getOrderItem().getReturnReceiverAddress());
			item.setReturnReceiverAddressDetail(temp.get(i).getOrderItem().getReturnReceiverAddressDetail());
			item.setReturnReceiverMessage(temp.get(i).getOrderItem().getReturnReceiverMessage());
			item.setReturnReceiverName(temp.get(i).getOrderItem().getReturnReceiverName());
			item.setReturnReceiverPhone(temp.get(i).getOrderItem().getReturnReceiverPhone());
			item.setReturnReceiverPostcode(temp.get(i).getOrderItem().getReturnReceiverPostcode());
			item.setReturnRegDate(temp.get(i).getOrderItem().getReturnRegDate());
			item.setReturnStatus(temp.get(i).getOrderItem().getReturnStatus());
			item.setReturnStatusCd(temp.get(i).getOrderItem().getReturnStatusCd());
			item.setReturnTrackingNumber(temp.get(i).getOrderItem().getReturnTrackingNumber());
			item.setSellMemberId(temp.get(i).getOrderItem().getSellMemberId());
			item.setSendDate(temp.get(i).getOrderItem().getSendDate());
			item.setStoreKey(temp.get(i).getOrderItem().getStoreKey());
			item.setStResCd(temp.get(i).getOrderItem().getStResCd());
			item.setStResNm(temp.get(i).getOrderItem().getStResNm());
			item.setTrackingNumber(temp.get(i).getOrderItem().getTrackingNumber());
			temp.get(i).setOrderItem(item);
			}
			
		}
		Page<PointHistory> result = new PageImpl<PointHistory>(temp, page,temp.size());
		
		return result;
		
	}
	
	
	
	public Page<PointHistory> getPointSummaryByMemberId(PageRequest page,int memberId){
		
		
		Page<PointHistory> list =  pointHistoryRepository.findAllByMemberId(page,memberId);
		
		return list;
		
	}
	
	
	
	
}
