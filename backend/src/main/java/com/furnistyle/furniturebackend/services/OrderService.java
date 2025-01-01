package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.OrderDTO;
import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.dtos.requests.OrderRequest;
import com.furnistyle.furniturebackend.enums.EOrderStatus;
import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByUserId(Long id);

    List<OrderDTO> getOrdersByStatus(EOrderStatus status);

    boolean createOrder(OrderRequest createOrderRequest);

    boolean updateStatus(Long orderId, EOrderStatus status);

    boolean updateConfirmAdmin(Long orderId, Long adminId);

    boolean updateAddress(Long orderId, String address);

    boolean updateOrderDetails(Long orderId, List<OrderDetailDTO> orderDetailDTOS);
}