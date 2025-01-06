package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.ReviewDTO;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.ReviewMapper;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.models.Review;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.repositories.OrderRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.repositories.ReviewRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.ReviewService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public void createReview(ReviewDTO reviewDTO) {
        User user = userRepository
            .findById(reviewDTO.getUserId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        Product product = productRepository.findById(reviewDTO.getProductId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));

        if (!orderRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            throw new BadRequestException(Constants.Message.NOT_BOUGHT);
        }

        Review review = reviewMapper.toEntity(reviewDTO);
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviewMapper.toDTOs(reviews);
    }
}
