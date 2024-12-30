package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long id);

    List<Product> findByMaterialId(Long id);

    Page<Product> findAll(Pageable pageable);
}
