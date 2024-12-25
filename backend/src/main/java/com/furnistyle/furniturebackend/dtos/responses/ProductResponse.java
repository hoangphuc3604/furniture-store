package com.furnistyle.furniturebackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("material_id")
    private Long materialId;
    private String origin;
    private String size;
    private String description;
}
