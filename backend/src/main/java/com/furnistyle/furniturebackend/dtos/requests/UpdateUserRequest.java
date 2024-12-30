package com.furnistyle.furniturebackend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private Long id;

    @NotNull(message = "Địa chỉ không được để trống!")
    private String address;

    @NotNull(message = "Họ tên không được để trống!")
    private String fullname;

    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có dạng '0123456789'!")
    private String phone;

    @Email(message = "Email không hợp lệ!")
    private String email;
}
