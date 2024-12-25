package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "Created customer's id is required")
    @JsonProperty("created_customer_id")
    private Long createdCustomerId;

    @NotNull(message = "Confirmed admin's id is required")
    @JsonProperty("confirmed_admin_id")
    private User confirmedAdminId;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Address is required")
    @Size(min = 1, max = 255, message = "Address must be between 1 and 255 characters")
    private String address;

    @NotNull(message = "Total amount is required")
    @JsonProperty("total_amount")
    private Double totalAmount;
}
