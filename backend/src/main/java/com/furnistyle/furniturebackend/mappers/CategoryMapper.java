package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryDTO> {
}
