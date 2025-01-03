package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MediaMapper.class)
public interface ProductMapper extends EntityMapper<Product, ProductDTO> {
    @Override
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "material.id", target = "materialId")
    @Mapping(source = "productImages", target = "productImages")
    ProductDTO toDTO(Product product);

    @Override
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "materialId", target = "material.id")
    @Mapping(source = "productImages", target = "productImages")
    Product toEntity(ProductDTO productDTO);
}
