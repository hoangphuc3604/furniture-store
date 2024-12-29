package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.exceptions.DataAccessException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.CategoryMapper;
import com.furnistyle.furniturebackend.mappers.UserMapper;
import com.furnistyle.furniturebackend.models.Category;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.CategoryRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        return categoryMapper.toDTO(categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy phân loại có id: " + id)));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDTOs(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId())
            .orElseThrow(() -> new NotFoundException("Không tìm thấy phân loại!"));
        category.setCategoryName(categoryDTO.getCategoryName());
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new DataAccessException("Không thể cập nhật thông tin phân loại!");
        }
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO deleteCategory(long id) throws Exception {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy phân loại!"));

        List<Product> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()) {
            throw new IllegalStateException("Không thể xóa phân loại vì có các phẩm liên quan!");
        } else {
            categoryRepository.deleteById(id);
            return categoryMapper.toDTO(category);
        }
    }
}
