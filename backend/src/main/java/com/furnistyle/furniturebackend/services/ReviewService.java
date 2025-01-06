package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.ReviewDTO;
import java.util.List;

public interface ReviewService {
    void createReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewsByProductId(Long productId);
}
