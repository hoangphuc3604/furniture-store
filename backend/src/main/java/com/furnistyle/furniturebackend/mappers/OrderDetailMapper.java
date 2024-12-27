package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.models.OrderDetail;
import com.furnistyle.furniturebackend.models.embeddedid.OrderDetailId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper extends EntityMapper<OrderDetail, OrderDetailDTO> {
    @Override
    @Mapping(source = "orderDetailId.orderId", target = "orderId")
    @Mapping(source = "orderDetailId.productId", target = "productId")
    OrderDetailDTO toDTO(OrderDetail orderDetail);

    @Override
    @Mapping(source = "orderId", target = "orderDetailId.orderId")
    @Mapping(source = "productId", target = "orderDetailId.productId")
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);

    default OrderDetailId toOrderDetailId(Long orderId, Long productId) {
        return new OrderDetailId(orderId, productId);
    }
}
