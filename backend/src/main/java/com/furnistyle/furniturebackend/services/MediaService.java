package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    MediaDTO createProductImage(Long productId, MediaDTO mediaDTO);

    List<MediaDTO> getProductImages(Long productId);

    MediaDTO getProductImageById(Long id);

    MediaDTO deleteProductImage(Long id);

    boolean createProductImages(Long productId, List<MultipartFile> files) throws Exception;
}
