package com.furnistyle.furniturebackend.services.impl;

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
import com.furnistyle.furniturebackend.services.ProductService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final ProductMapper productMapper;
    private final ProductResponseMapper productResponseMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (!categoryRepository.existsById(productDTO.getCategoryId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_CATEGORY);
        }
        if (!materialRepository.existsById(productDTO.getMaterialId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_MATERIAL);
        }
        productDTO.setStatus(EProductStatus.IN_STOCK);
        productRepository.save(productMapper.toEntity(productDTO));
        return productDTO;
    }

    @Override
    public ProductResponse getProductById(long id) {
        Product existingProduct = productRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));
        return productResponseMapper.toDTO(existingProduct);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId,
                                                Long materialId,
                                                PageRequest pageRequest) {
        Page<Product> productPage;
        productPage = productRepository.searchProducts(keyword, categoryId, materialId, pageRequest);
        return productPage.map(productResponseMapper::toDTO);
    }

    @Override
    public List<ProductResponse> getRelatedProducts(Long currentProductId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Product product = productRepository.findById(currentProductId)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));
        List<Product> relatedProducts = productRepository
            .findRelatedProducts(currentProductId,
                product.getCategory().getId(),
                product.getMaterial().getId(), pageable);
        return relatedProducts.stream()
            .map(productResponseMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getNewProducts(int limit) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> newProducts = productRepository.findNewProducts(oneMonthAgo, pageable);
        return newProducts.stream()
            .map(productResponseMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        if (!productRepository.existsById(productDTO.getId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT);
        }

        if (!categoryRepository.existsById(productDTO.getCategoryId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_CATEGORY);
        }

        if (!materialRepository.existsById(productDTO.getMaterialId())) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_MATERIAL);
        }

        productRepository.save(productMapper.toEntity(productDTO));
        return productDTO;
    }

    @Override
    public boolean deleteProduct(long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));
        product.setStatus(EProductStatus.OUT_OF_STOCK);
        productRepository.save(product);
        return true;
    }
}
