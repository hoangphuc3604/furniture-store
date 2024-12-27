package com.furnistyle.furniturebackend.models;

import com.furnistyle.furniturebackend.models.embeddedid.CartDetailId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_details")
public class CartDetail {
    @EmbeddedId
    private CartDetailId id;

    @ManyToOne
    @MapsId("ownerId")
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer amount;

    @PrePersist
    protected void checkAmount() {
        amount = 1;
    }
}
