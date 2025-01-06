package com.furnistyle.furniturebackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.dtos.bases.CategoryDTO;
import com.furnistyle.furniturebackend.dtos.bases.MaterialDTO;
import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.enums.EProductStatus;
import java.util.List;
import lombok.Data;

@Data
public class ProductResponse {
    Long id;
    private String name;
    private Double price;
    @JsonProperty("category_id")
    private CategoryDTO category;
    @JsonProperty("material_id")
    private MaterialDTO material;
    private String origin;
    private String size;
    private String description;
    private EProductStatus status = EProductStatus.ACTIVE;
    @JsonProperty("product_images")
    private List<MediaDTO> productImages;
}
