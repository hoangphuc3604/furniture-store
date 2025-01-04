package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.OTP;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
    OTP getByEmail(@Email String email);
}