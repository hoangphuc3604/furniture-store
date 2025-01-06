package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.dtos.responses.ProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductResponse getProductById(long id);

    List<ProductResponse> getRelatedProducts(Long currentProductId, int limit);

    List<ProductResponse> getNewProducts(int limit);

    ProductDTO updateProduct(ProductDTO productDTO);

    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, Long materialId, PageRequest pageRequest);

    boolean deleteProduct(long id);
}
