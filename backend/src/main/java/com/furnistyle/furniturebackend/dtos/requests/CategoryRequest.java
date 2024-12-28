package com.furnistyle.furniturebackend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
//    @NotBlank(message = "Tên phân loại không được để trống!")
//    @Size(min = 1, max = 100, message = "Tên phân loại phải từ 1 đến 100 ký tự!")
//    @JsonProperty("category_name")
//    private String categoryName;
}
