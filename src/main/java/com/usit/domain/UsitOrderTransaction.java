package com.usit.domain;

import java.util.List;

public class UsitOrderTransaction {

    private UsitOrder usitOrder;
    private List<UsitOrderItem> usitOrderItems;
    private List<DeliveryFee> deliveryFees;

    public List<DeliveryFee> getDeliveryFees() {
		return deliveryFees;
	}
	public void setDeliveryFees(List<DeliveryFee> deliveryFees) {
		this.deliveryFees = deliveryFees;
	}
	public UsitOrder getUsitOrder() {
        return usitOrder;
    }
    public void setUsitOrder(UsitOrder usitOrder) {
        this.usitOrder = usitOrder;
    }
    public List<UsitOrderItem> getUsitOrderItems() {
        return usitOrderItems;
    }
    public void setUsitOrderItems(List<UsitOrderItem> usitOrderItems) {
        this.usitOrderItems = usitOrderItems;
    }

}
