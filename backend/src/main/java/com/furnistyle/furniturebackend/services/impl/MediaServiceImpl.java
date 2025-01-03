package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.dtos.bases.ProductDTO;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.MediaMapper;
import com.furnistyle.furniturebackend.models.Media;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.MediaRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.CloudinaryService;
import com.furnistyle.furniturebackend.services.MediaService;
import com.furnistyle.furniturebackend.services.ProductService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public MediaDTO createProductImage(Long productId, MediaDTO mediaDTO) {
        Product existingProduct = productRepository
            .findById(productId)
            .orElseThrow(() ->
                new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));
        Media newMedia = Media.builder()
            .product(existingProduct)
            .imageLink(mediaDTO.getImageLink())
            .build();

        int size = mediaRepository.findByProductId(productId).size();
        if (size >= Constants.Message.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new BadRequestException(Constants.Message.UPLOAD_IMAGES_MAX_20);
        }

        productRepository.save(existingProduct);
        mediaRepository.save(newMedia);
        return mediaDTO;
    }

    @Override
    public boolean createProductImages(Long productId, List<MultipartFile> files) throws Exception {
        ProductDTO existingProduct = productService.getProductById(productId);
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if (files.size() > Constants.Message.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new Exception(Constants.Message.UPLOAD_IMAGES_MAX_20);
        }
        List<MediaDTO> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > Constants.Message.MAXIMUM_SIZE_PER_PRODUCT * 1024 * 1024) {
                throw new Exception(Constants.Message.UPLOAD_IMAGES_FILE_LARGE);
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new Exception(Constants.Message.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE);
            }

            String filename = cloudinaryService.storeFile(file);

            MediaDTO productImage = createProductImage(
                existingProduct.getId(),
                MediaDTO.builder()
                    .imageLink(filename)
                    .build()
            );
            productImages.add(productImage);
        }
        return true;
    }

    public List<MediaDTO> getProductImages(Long productId) {
        return mediaMapper.toDTOs(mediaRepository.findByProductId(productId));
    }

    public MediaDTO getProductImageById(Long id) {
        Media media = mediaRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_DATABASE_IMAGES));
        return mediaMapper.toDTO(media);
    }

    @Override
    public MediaDTO deleteProductImage(Long id) {
        Optional<Media> productImage = mediaRepository.findById(id);
        if (productImage.isEmpty()) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_DATABASE_IMAGES);
        }
        mediaRepository.deleteById(id);
        return mediaMapper.toDTO(productImage.get());
    }
}
