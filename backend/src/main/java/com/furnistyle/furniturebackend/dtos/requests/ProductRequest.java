package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater or equal to 0")
    private Double price;

    @NotNull(message = "Category is required")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull(message = "Material is required")
    @JsonProperty("material_id")
    private Long materialId;

    @NotBlank(message = "Origin is required")
    @Size(min = 1, max = 100, message = "Origin must be between 1 and 100 characters")
    private String origin;

    @NotBlank(message = "Size is required")
    @Size(min = 1, max = 50, message = "Size must be between 1 and 100 characters")
    private String size;

    private String description;

}
