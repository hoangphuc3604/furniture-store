package com.furnistyle.furniturebackend.dtos.bases;

import lombok.Data;

@Data
public class CartDetailDTO {
    private Long ownerId;
    private Long productId;
    private Integer amount;
}
