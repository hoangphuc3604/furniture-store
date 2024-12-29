package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductDTO> {
}
