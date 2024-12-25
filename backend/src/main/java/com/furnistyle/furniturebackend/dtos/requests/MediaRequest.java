package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MediaRequest {
    @NotNull(message = "Product's id is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotBlank(message = "Image's link is required")
    @JsonProperty("image_link")
    private String imageLink;
}
