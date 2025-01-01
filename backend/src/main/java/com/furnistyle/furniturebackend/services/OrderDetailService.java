package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import java.util.List;

public interface OrderDetailService {
    List<OrderDetailDTO> getAllByOrderId(Long orderId);

    void createOrderDetail(OrderDetailDTO orderDetailDTO);

    void updateOrderDetail(OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(Long orderId, Long productId);
}