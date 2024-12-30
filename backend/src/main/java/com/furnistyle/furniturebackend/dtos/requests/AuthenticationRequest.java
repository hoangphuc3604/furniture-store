package com.furnistyle.furniturebackend.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull(message = "Tên tài khoản không được trống!")
    String username;

    @NotNull(message = "Mật khẩu không được để trống!")
    String password;
}
