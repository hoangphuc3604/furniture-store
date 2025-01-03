package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.services.CloudinaryService;
import com.furnistyle.furniturebackend.services.MediaService;
import com.furnistyle.furniturebackend.services.ProductService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/medias")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
        @RequestParam Long productId,
        @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            mediaService.createProductImages(productId, files);
            return ResponseEntity.ok().body(Constants.Message.UPLOAD_IMAGES_FILE_SUCCESSFUL);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> getProductImageById(@PathVariable Long id) {
        return ResponseEntity.ok().body(mediaService.getProductImageById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<MediaDTO>> getProductImages(@RequestParam Long productId) {
        return ResponseEntity.ok().body(mediaService.getProductImages(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            MediaDTO productImage = mediaService.deleteProductImage(id);
            if (productImage != null) {
                cloudinaryService.deleteFile(productImage.getImageLink());
            }
            return ResponseEntity.ok(productImage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}
