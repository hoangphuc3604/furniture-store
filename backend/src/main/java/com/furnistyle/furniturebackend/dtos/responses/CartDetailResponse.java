package com.furnistyle.furniturebackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponse {
    private Long id;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("product_id")
    private Long productId;
    private Integer amount;
}
