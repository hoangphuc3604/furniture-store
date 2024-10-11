package com.furnistyle.furniturebackend.repository;

import com.furnistyle.furniturebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
