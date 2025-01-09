package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.dtos.responses.ProductListResponse;
import com.furnistyle.furniturebackend.dtos.responses.ProductResponse;
import com.furnistyle.furniturebackend.services.ProductService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<List<ProductResponse>> getRelatedProducts(
        @PathVariable("id") Long productId) {
        List<ProductResponse> relatedProducts = productService.getRelatedProducts(productId, 5);
        return ResponseEntity.ok(relatedProducts);
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts(
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ProductResponse> newProducts = productService.getNewProducts(limit);
        return ResponseEntity.ok(newProducts);
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
        @RequestParam(defaultValue = "0", name = "material_id") Long materialId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<ProductResponse> productResponsePage = productService
            .getAllProducts(keyword, categoryId, materialId, pageRequest);
        return ResponseEntity.ok(ProductListResponse
            .builder()
            .products(productResponsePage.getContent())
            .totalPages(productResponsePage.getTotalPages())
            .build());
    }

    @PutMapping("")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return ResponseEntity.ok(Constants.Message.UPDATE_PRODUCT_SUCCESSFUL);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(Constants.Message.DELETE_PRODUCT_SUCCESSFUL);
    }
}
