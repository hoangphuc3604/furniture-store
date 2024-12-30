package com.furnistyle.furniturebackend.dtos.bases;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MaterialDTO {
    Long id;

    @NotBlank(message = "Tên chất liệu không được để trống!")
    @Size(min = 1, max = 100, message = "Tên chất liệu phải từ 1 đến 100 ký tự!")
    @JsonProperty("material_name")
    private String materialName;
}
