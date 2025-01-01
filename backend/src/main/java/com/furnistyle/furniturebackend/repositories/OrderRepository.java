package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.models.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findByCreatedCustomerId(Long id);

    List<Order> findByStatus(EOrderStatus status);
}