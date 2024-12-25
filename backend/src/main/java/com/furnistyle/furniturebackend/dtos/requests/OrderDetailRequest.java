package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailRequest {
    @NotNull(message = "Order's id is required")
    @JsonProperty("order_id")
    private Long orderId;

    @NotNull(message = "Product's id is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Amount is required")
    private Integer amount;

    @NotNull(message = "Price is required")
    private Double price;
}
