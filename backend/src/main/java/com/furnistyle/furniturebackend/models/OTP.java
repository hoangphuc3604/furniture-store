package com.furnistyle.furniturebackend.models;

import com.furnistyle.furniturebackend.enums.ETypeOfOTP;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otps")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Email
    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false, length = 6)
    String code;

    @Enumerated(EnumType.STRING)
    ETypeOfOTP type;

    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdDate;
}
