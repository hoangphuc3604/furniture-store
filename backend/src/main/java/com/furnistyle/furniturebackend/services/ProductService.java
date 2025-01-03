package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(long id);

    ProductDTO updateProduct(ProductDTO productDTO);

    Page<ProductDTO> getAllProducts(String keyword, Long categoryId, Long materialId, PageRequest pageRequest);

    boolean deleteProduct(long id);
}
