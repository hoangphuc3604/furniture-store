package com.furnistyle.furniturebackend.dtos.bases;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    Long id;

    @NotNull(message = "ID sản phẩm không được để trống!")
    @JsonProperty("product_id")
    private Long productId;

    @NotBlank(message = "Link ảnh không được để trống!")
    @JsonProperty("image_link")
    private String imageLink;
}
