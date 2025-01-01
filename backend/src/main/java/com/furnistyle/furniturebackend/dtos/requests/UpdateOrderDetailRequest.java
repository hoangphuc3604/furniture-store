package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UpdateOrderDetailRequest {
    @JsonProperty("order_id")
    Long orderId;

    @JsonProperty("details")
    List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
}