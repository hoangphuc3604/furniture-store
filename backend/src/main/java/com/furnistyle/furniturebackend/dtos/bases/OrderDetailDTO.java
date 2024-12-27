package com.furnistyle.furniturebackend.dtos.bases;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long orderId;
    private Long productId;
    private Integer amount;
    private Double price;
}
