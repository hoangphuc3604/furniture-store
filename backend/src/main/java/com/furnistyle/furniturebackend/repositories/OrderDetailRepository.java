package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.OrderDetail;
import com.furnistyle.furniturebackend.models.embeddedid.OrderDetailId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    void deleteOrderDetailByOrderDetailId(OrderDetailId orderDetailId);

    List<OrderDetail> findByOrderId(Long orderId);
}