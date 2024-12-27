package com.furnistyle.furniturebackend.models.embeddedid;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailId implements Serializable {
    private Long ownerId;
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
        return ownerId.equals(that.ownerId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return 31 * ownerId.hashCode() + productId.hashCode();
    }
}
