package com.furnistyle.furniturebackend.dtos.bases;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;

    @NotNull(message = "Product ID không được để trống!")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "User ID không được để trống!")
    @JsonProperty("user_id")
    private Long userId;

    @Min(value = 1, message = "Rating nhỏ nhất phải bằng 1!")
    @Max(value = 5, message = "Rating lớn nhất phải bằng 5!")
    private int rating;

    private String comment;
}
