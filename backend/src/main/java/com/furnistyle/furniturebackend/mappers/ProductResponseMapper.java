package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.dtos.responses.ProductResponse;
import com.furnistyle.furniturebackend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MediaMapper.class)
public interface ProductResponseMapper extends EntityMapper<Product, ProductResponse> {
    @Override
    @Mapping(source = "productImages", target = "productImages")
    ProductResponse toDTO(Product product);

    @Override
    @Mapping(source = "productImages", target = "productImages")
    Product toEntity(ProductResponse productResponse);
}

