package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "Tên tài khoản không được trống!")
    String username;

    @NotNull(message = "Mật khẩu không được để trống!")
    String password;

    @NotNull(message = "Địa chỉ không được để trống!")
    private String address;

    @NotNull(message = "Họ tên không được để trống!")
    private String fullname;

    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có dạng '0123456789'!")
    private String phone;

    @NotNull(message = "Email không được để trống!")
    @Email(message = "Email phải có dạng 'name@domain'!")
    private String email;

    @Past(message = "Ngày sinh phải trong quá khứ!")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(MALE|FEMALE)$", message = "Giới tính phải là 'MALE' hoặc 'FEMALE'!")
    private String gender;

    private String role;
}
