package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.CartDetailDTO;
import com.furnistyle.furniturebackend.models.CartDetail;
import com.furnistyle.furniturebackend.models.embeddedid.CartDetailId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartDetailMapper extends EntityMapper<CartDetail, CartDetailDTO> {
    @Override
    @Mapping(source = "id.ownerId", target = "ownerId")
    @Mapping(source = "id.productId", target = "productId")
    CartDetailDTO toDTO(CartDetail cartDetail);

    @Override
    @Mapping(source = "ownerId", target = "id.ownerId")
    @Mapping(source = "productId", target = "id.productId")
    CartDetail toEntity(CartDetailDTO cartDetailDTO);

    default CartDetailId toCartDetailId(Long ownerId, Long productId) {
        return new CartDetailId(ownerId, productId);
    }
}
