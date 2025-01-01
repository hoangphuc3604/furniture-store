package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.OrderDetailMapper;
import com.furnistyle.furniturebackend.models.OrderDetail;
import com.furnistyle.furniturebackend.models.embeddedid.OrderDetailId;
import com.furnistyle.furniturebackend.repositories.OrderDetailRepository;
import com.furnistyle.furniturebackend.services.OrderDetailService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetailDTO> getAllByOrderId(Long orderId) {
        return orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(orderId));
    }

    @Override
    public void createOrderDetail(OrderDetailDTO orderDetailDTO) {
        orderDetailRepository.save(orderDetailMapper.toEntity(orderDetailDTO));
    }

    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail;
        orderDetail = orderDetailRepository.findById(
                new OrderDetailId(orderDetailDTO.getOrderId(), orderDetailDTO.getProductId()))
            .orElseThrow(() -> new NotFoundException(
                Constants.Message.NOT_FOUND_PRODUCT_IN_ORDER));
        orderDetail.setAmount(orderDetailDTO.getAmount());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Long orderId, Long productId) {
        orderDetailRepository.deleteOrderDetailByOrderDetailId(
            new OrderDetailId(orderId, productId));
    }
}