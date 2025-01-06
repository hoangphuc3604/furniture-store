package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
