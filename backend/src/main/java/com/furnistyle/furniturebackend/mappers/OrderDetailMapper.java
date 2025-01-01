package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.models.OrderDetail;
import com.furnistyle.furniturebackend.models.embeddedid.OrderDetailId;
import com.furnistyle.furniturebackend.repositories.OrderRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.utils.Constants;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper extends EntityMapper<OrderDetail, OrderDetailDTO> {
    @Override
    @Mapping(source = "orderDetailId.orderId", target = "orderId")
    @Mapping(source = "orderDetailId.productId", target = "productId")

    OrderDetailDTO toDTO(OrderDetail orderDetail);

    @Override
    @Mapping(source = "orderId", target = "orderDetailId.orderId")
    @Mapping(source = "productId", target = "orderDetailId.productId")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);

    default OrderDetailId toOrderDetailId(Long orderId, Long productId) {
        return new OrderDetailId(orderId, productId);
    }

    @AfterMapping
    default void mapAdditionalFields(OrderDetailDTO orderDetailDTO, @MappingTarget OrderDetail orderDetail,
                                     @Context OrderRepository orderRepository,
                                     @Context ProductRepository productRepository) {
        orderDetail.setOrder(orderRepository.findById(orderDetailDTO.getOrderId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_ORDER)));
        orderDetail.setProduct(productRepository.findById(orderDetailDTO.getProductId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT)));
    }
}
