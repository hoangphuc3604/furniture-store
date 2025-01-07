package com.furnistyle.furniturebackend.dtos.bases;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnistyle.furniturebackend.enums.EProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {
    Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống!")
    @Size(min = 1, max = 200, message = "Độ dài tên sản phẩm phải từ 1 đến 200 ký tự!")
    @JsonProperty("name")
    private String name;

    @Min(value = 0, message = "Giá tiền phải lớn hơn hoặc bằng 0!")
    private Double price;

    @NotNull(message = "Phân loại không được để trống!")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull(message = "Chất liệu không được để trống!")
    @JsonProperty("material_id")
    private Long materialId;

    @NotBlank(message = "Xuất xứ không được để trống!")
    @Size(min = 1, max = 100, message = "Xuất xứ phải từ 1 đến 100 ký tự!")
    private String origin;

    @NotBlank(message = "Kích cỡ không được để trống!")
    @Size(min = 1, max = 50, message = "Kích cỡ phải từ 1 đến 50 ký tự!")
    private String size;

    private String description;

    private EProductStatus status;
}
