package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.dtos.responses.ProductResponse;
import com.furnistyle.furniturebackend.enums.EProductStatus;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.ProductMapper;
import com.furnistyle.furniturebackend.mappers.ProductResponseMapper;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.CategoryRepository;
import com.furnistyle.furniturebackend.repositories.MaterialRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.impl.ProductServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void getProductById_WhenProductNotFound_ThenThrowNotFoundException() {
        long productId = 1L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> productService.getProductById(productId)
        );

        assertEquals(Constants.Message.NOT_FOUND_PRODUCT, exception.getMessage());
    }

    @Test
    void getProductById_WhenProductFound_ThenReturnProductResponse() {
        long productId = 1L;
        Product product = Instancio.create(Product.class);
        ProductResponse productResponse = Instancio.create(ProductResponse.class);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(productResponseMapper.toDTO(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductById(productId);

        assertEquals(productResponse, result);
    }

    @Test
    void createProduct_WhenCategoryOrMaterialNotFound_ThenThrowNotFoundException() {
        ProductDTO productDTO = Instancio.create(ProductDTO.class);
        Mockito.when(categoryRepository.existsById(productDTO.getCategoryId())).thenReturn(false);

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> productService.createProduct(productDTO)
        );

        assertEquals(Constants.Message.NOT_FOUND_CATEGORY, exception.getMessage());
    }

    @Test
    void createProduct_WhenValidRequest_ThenReturnCreatedProductDTO() {
        ProductDTO productDTO = Instancio.create(ProductDTO.class);
        Product product = Instancio.create(Product.class);

        Mockito.when(categoryRepository.existsById(productDTO.getCategoryId())).thenReturn(true);
        Mockito.when(materialRepository.existsById(productDTO.getMaterialId())).thenReturn(true);
        Mockito.when(productMapper.toEntity(productDTO)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(productDTO);

        assertEquals(productDTO, result);
    }

    @Test
    void deleteProduct_WhenProductNotFound_ThenThrowNotFoundException() {
        long productId = 1L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> productService.deleteProduct(productId)
        );

        assertEquals(Constants.Message.NOT_FOUND_PRODUCT, exception.getMessage());
    }

    @Test
    void deleteProduct_WhenProductFound_ThenSetStatusInactive() {
        long productId = 1L;
        Product product = Instancio.create(Product.class);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        boolean result = productService.deleteProduct(productId);

        assertTrue(result);
        assertEquals(EProductStatus.INACTIVE, product.getStatus());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void getAllProducts_WhenValidRequest_ThenReturnPagedProductResponses() {
        String keyword = "table";
        Long categoryId = 1L;
        Long materialId = 2L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> products = Instancio.ofList(Product.class).size(5).create();
        Page<Product> productPage = new PageImpl<>(products);
        List<ProductResponse> productResponses = Instancio.ofList(ProductResponse.class).size(5).create();

        Mockito.when(productRepository.searchProducts(keyword, categoryId, materialId, pageRequest)).thenReturn(productPage);
        Mockito.when(productResponseMapper.toDTO(Mockito.any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return Instancio.create(ProductResponse.class);
        });

        Page<ProductResponse> result = productService.getAllProducts(keyword, categoryId, materialId, pageRequest);

        assertEquals(productResponses.size(), result.getContent().size());
    }

    @Test
    void getNewProducts_WhenCalled_ThenReturnNewProductResponses() {
        int limit = 5;
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Product> newProducts = Instancio.ofList(Product.class).size(5).create();

        Mockito.when(productRepository.findNewProducts(Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
            .thenReturn(newProducts);

        Mockito.when(productResponseMapper.toDTO(Mockito.any(Product.class)))
            .thenAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                ProductResponse response = new ProductResponse();
                response.setId(product.getId()); // Map cơ bản
                return response;
            });

        List<ProductResponse> result = productService.getNewProducts(limit);

        assertEquals(newProducts.size(), result.size());
    }
}
