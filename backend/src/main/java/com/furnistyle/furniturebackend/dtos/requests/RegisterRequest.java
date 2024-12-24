package com.furnistyle.furniturebackend.dtos.requests;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String address;
    private String fullname;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String role;
    private boolean status;
}
