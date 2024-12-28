package com.furnistyle.furniturebackend.dtos.bases;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderDTO {
    private Long id;

    @NotNull(message = "Khách hàng tạo đơn không được để trống")
    @JsonProperty("created_customer_id")
    private Long createdCustomerId;

    @NotNull(message = "Admin xác nhận không được để trống")
    @JsonProperty("confirmed_admin_id")
    private User confirmedAdminId;

    @NotBlank(message = "Trạng thái không được để trống!")
    private String status;

    @NotBlank(message = "Địa chi không được để trống!")
    @Size(min = 1, max = 255, message = "Địa chỉ phải nằm từ 1 đến 255 ký tự")
    private String address;

    @NotNull(message = "Tổng tiền không được để trống!")
    @JsonProperty("total_amount")
    private Double totalAmount;
}
