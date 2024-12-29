package com.furnistyle.furniturebackend.dtos.bases;

import com.furnistyle.furniturebackend.enums.EGender;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.enums.EUserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Tên tài khoản không được để trống!")
    private String username;

    @NotNull(message = "Mật khẩu không được để trống!")
    private String password;

    private String address;

    private String fullname;

    @Size(min = 10, max = 10, message = "Số điện thoại có 10 chữ số!")
    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Past(message = "Ngày sinh phải là trong quá khứ")
    private LocalDate dateOfBirth;

    private EGender gender;
    private ERole role;
    private EUserStatus status;
}
