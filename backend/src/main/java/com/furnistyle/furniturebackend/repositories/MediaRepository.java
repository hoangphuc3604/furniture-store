package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Media;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByProductId(Long productId);
}
