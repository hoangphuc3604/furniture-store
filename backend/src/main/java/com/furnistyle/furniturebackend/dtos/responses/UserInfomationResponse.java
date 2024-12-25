package com.furnistyle.furniturebackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class UserInfomationResponse {
    private Long id;
    private String address;
    private String fullname;
    private String phone;
    private String email;
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    private String gender;
}
