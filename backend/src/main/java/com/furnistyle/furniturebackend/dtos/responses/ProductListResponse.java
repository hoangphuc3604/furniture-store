package com.furnistyle.furniturebackend.dtos.responses;

import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductDTO> products;
    private int totalPages;
}
