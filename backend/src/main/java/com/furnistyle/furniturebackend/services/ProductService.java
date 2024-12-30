package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(long id);

    ProductDTO updateProduct(ProductDTO productDTO);

    Page<ProductDTO> getAllProducts(PageRequest pageRequest);

    boolean deleteProduct(long id);
}
