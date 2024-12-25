package com.furnistyle.furniturebackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    @JsonProperty("created_customer_id")
    private Long createdCustomerId;
    @JsonProperty("confirmed_admin_id")
    private User confirmedAdminId;
    private String status;
    private String address;
    @JsonProperty("total_amount")
    private Double totalAmount;
}
