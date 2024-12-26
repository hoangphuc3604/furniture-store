package com.furnistyle.furniturebackend.models.embeddedid;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderDetailId {
    private Long orderId;
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetailId that = (OrderDetailId) o;
        return orderId.equals(that.orderId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return 31 * orderId.hashCode() + productId.hashCode();
    }
}
