package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.models.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findByCreatedCustomerId(Long id);

    List<Order> findByStatus(EOrderStatus status);

    List<Order> findAllByStatus(EOrderStatus status);

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.orderItems i "
        + "WHERE o.createdCustomer.id = :userId AND i.product.id = :productId AND o.status = 'DELIVERED'")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}