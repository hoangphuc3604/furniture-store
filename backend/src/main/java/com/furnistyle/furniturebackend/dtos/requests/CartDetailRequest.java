package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDetailRequest {
    @NotNull(message = "Customer's id is required")
    @JsonProperty("customer_id")
    private Long customerId;

    @NotNull(message = "Product's id is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Amount id is required")
    private Integer amount;

}
