package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.CategoryMapper;
import com.furnistyle.furniturebackend.models.Category;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.CategoryRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.impl.CategoryServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    void getCategoryById_WhenCategoryNotFound_ThenThrowNotFoundException() {
        long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> categoryService.getCategoryById(categoryId)
        );

        assertEquals(Constants.Message.NOT_FOUND_CATEGORY, exception.getMessage());
    }

    @Test
    void getCategoryById_WhenCategoryFound_ThenReturnCategoryDTO() {
        long categoryId = 1L;
        Category category = Instancio.create(Category.class);
        CategoryDTO categoryDTO = Instancio.create(CategoryDTO.class);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(categoryId);

        assertEquals(categoryDTO, result);
    }

    @Test
    void createCategory_WhenValidRequest_ThenReturnCreatedCategoryDTO() {
        CategoryDTO categoryDTO = Instancio.create(CategoryDTO.class);
        Category category = Instancio.create(Category.class);

        Mockito.when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertEquals(categoryDTO, result);
    }

    @Test
    void updateCategory_WhenCategoryNotFound_ThenThrowNotFoundException() {
        CategoryDTO categoryDTO = Instancio.create(CategoryDTO.class);

        Mockito.when(categoryRepository.existsById(categoryDTO.getId())).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> categoryService.updateCategory(categoryDTO)
        );

        assertEquals(Constants.Message.NOT_FOUND_CATEGORY, exception.getMessage());
    }

    @Test
    void updateCategory_WhenValidRequest_ThenReturnUpdatedCategoryDTO() {
        CategoryDTO categoryDTO = Instancio.create(CategoryDTO.class);
        Category category = Instancio.create(Category.class);

        Mockito.when(categoryRepository.existsById(categoryDTO.getId())).thenReturn(true);
        Mockito.when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.updateCategory(categoryDTO);

        assertEquals(categoryDTO, result);
    }

    @Test
    void deleteCategory_WhenCategoryNotFound_ThenThrowNotFoundException() {
        long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals(Constants.Message.NOT_FOUND_CATEGORY, exception.getMessage());
    }

    @Test
    void deleteCategory_WhenCategoryHasProducts_ThenThrowIllegalStateException() {
        long categoryId = 1L;
        Category category = Instancio.create(Category.class);
        List<Product> products = Instancio.ofList(Product.class).size(3).create();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findByCategoryId(categoryId)).thenReturn(products);

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals(Constants.Message.DELETE_CATEGORY_FAILED, exception.getMessage());
    }

    @Test
    void deleteCategory_WhenCategoryHasNoProducts_ThenDeleteSuccessfully() throws Exception {
        long categoryId = 1L;

        Category category = Instancio.create(Category.class);
        category.setId(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);
        categoryDTO.setCategoryName("Test Category");
        Mockito.when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.deleteCategory(categoryId);

        assertEquals(categoryDTO.getId(), result.getId());
        assertEquals(categoryDTO.getCategoryName(), result.getCategoryName());
        Mockito.verify(categoryRepository).deleteById(categoryId);
    }
}
