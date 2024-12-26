package com.furnistyle.furniturebackend.models.embeddedid;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;

@Embeddable
@Data
public class CartDetailId implements Serializable {
    private Long userId;
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartDetailId that = (CartDetailId) o;
        return userId.equals(that.userId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return 31 * userId.hashCode() + productId.hashCode();
    }
}
