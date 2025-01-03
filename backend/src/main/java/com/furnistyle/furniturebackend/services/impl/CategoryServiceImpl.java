package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.exceptions.DataAccessException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.CategoryMapper;
import com.furnistyle.furniturebackend.models.Category;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.CategoryRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.CategoryService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return categoryMapper.toDTO(categoryRepository.save(categoryMapper.toEntity(categoryDTO)));
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        return categoryMapper.toDTO(categoryRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_CATEGORY)));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDTOs(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        if (!categoryRepository.existsById(categoryDTO.getId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_CATEGORY);
        }
        try {
            categoryRepository.save(categoryMapper.toEntity(categoryDTO));
        } catch (Exception e) {
            throw new DataAccessException(Constants.Message.UPDATE_CATEGORY_FAILED);
        }
        return categoryMapper.toDTO(categoryMapper.toEntity(categoryDTO));
    }

    @Override
    public CategoryDTO deleteCategory(long id) throws Exception {
        Category category = categoryRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_CATEGORY));

        List<Product> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()) {
            throw new IllegalStateException(Constants.Message.DELETE_CATEGORY_FAILED);
        } else {
            categoryRepository.deleteById(id);
            return categoryMapper.toDTO(category);
        }
    }
}
