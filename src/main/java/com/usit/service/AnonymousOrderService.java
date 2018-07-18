package com.usit.service;


import java.util.List;

import com.usit.domain.AnonymousOrder;

public interface AnonymousOrderService {

//	AnonymousOrder getAnonymousOrder(Long memberId);

	AnonymousOrder createAnonymousOrder(AnonymousOrder anonymousOrder);

	AnonymousOrder updateAnonymousOrder(AnonymousOrder anonymousOrder,int anonymousOrderId,int memberId);

	void deleteAnonymousOrder(int anonymousOrderId);


}