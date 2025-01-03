package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long id);

    List<Product> findByMaterialId(Long id);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE "
        + "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) "
        + "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%) "
        + "AND (:materialId IS NULL OR :materialId = 0 OR p.material.id = :materialId) "
        + "AND p.status = 'ACTIVE'")
    Page<Product> searchProducts(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("materialId") Long materialId,
        Pageable pageable);
}
