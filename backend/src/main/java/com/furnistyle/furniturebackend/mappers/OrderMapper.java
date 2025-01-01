package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.OrderDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.models.Order;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.utils.Constants;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrderMapper extends EntityMapper<Order, OrderDTO> {
    @Mapping(source = "createdCustomer.id", target = "createdCustomerId")
    @Mapping(source = "confirmedAdmin.id", target = "confirmedAdminId")
    OrderDTO toDTO(Order order);

    @Mapping(target = "createdCustomer", ignore = true)
    @Mapping(target = "confirmedAdmin", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @AfterMapping
    default void mapAdditionalFields(OrderDTO orderDTO, @MappingTarget Order order,
                                     @Context UserRepository userRepository) {
        order.setCreatedCustomer(userRepository.findById(orderDTO.getCreatedCustomerId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER)));
        order.setConfirmedAdmin(userRepository.findById(orderDTO.getConfirmedAdminId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER)));
    }
}