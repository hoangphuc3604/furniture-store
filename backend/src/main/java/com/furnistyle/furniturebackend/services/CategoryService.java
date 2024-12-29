package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.models.Category;
import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(long id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(long id) throws Exception;
}
