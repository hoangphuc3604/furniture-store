package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Khách hàng tạo đơn không được để trống")
    @JsonProperty("created_customer_id")
    private Long createdCustomerId;

    @NotBlank(message = "Địa chi không được để trống!")
    @Size(min = 1, max = 255, message = "Địa chỉ phải nằm từ 1 đến 255 ký tự")
    private String address;

    @NotNull(message = "danh sách chi tiết khôgn được để trống!")
    List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
}