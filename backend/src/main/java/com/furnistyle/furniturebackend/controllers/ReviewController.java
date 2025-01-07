package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.ReviewDTO;
import com.furnistyle.furniturebackend.services.ReviewService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<String> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        reviewService.createReview(reviewDTO);
        return ResponseEntity.ok(Constants.Message.ADD_REVIEW_SUCCESSFUL);
    }

    @GetMapping("")
    public ResponseEntity<?> getProductById(@RequestParam("product_id") Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }
}
