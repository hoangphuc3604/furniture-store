package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.MediaMapper;
import com.furnistyle.furniturebackend.models.Media;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.MediaRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.impl.MediaServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private MediaMapper mediaMapper;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private MediaServiceImpl mediaService;

    @Test
    void createProductImage_WhenProductNotFound_ThenThrowNotFoundException() {
        Long productId = 1L;
        MediaDTO mediaDTO = MediaDTO.builder().imageLink("image.jpg").build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> mediaService.createProductImage(productId, mediaDTO)
        );

        assertEquals(Constants.Message.NOT_FOUND_PRODUCT, exception.getMessage());
    }

    @Test
    void createProductImage_WhenMaxImagesReached_ThenThrowBadRequestException() {
        Long productId = 1L;
        MediaDTO mediaDTO = MediaDTO.builder().imageLink("image.jpg").build();

        Product product = mock(Product.class);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mediaRepository.findByProductId(productId)).thenReturn(
            List.of(new Media(), new Media(), new Media(), new Media(), new Media(),
                new Media(), new Media(), new Media(), new Media(), new Media(),
                new Media(), new Media(), new Media(), new Media(), new Media(),
                new Media(), new Media(), new Media(), new Media(), new Media())
        );

        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> mediaService.createProductImage(productId, mediaDTO)
        );

        assertEquals(Constants.Message.UPLOAD_IMAGES_MAX_20, exception.getMessage());
    }

    @Test
    void createProductImage_WhenValidRequest_ThenSaveMedia() {
        Long productId = 1L;
        MediaDTO mediaDTO = MediaDTO.builder().imageLink("image.jpg").build();

        Product product = mock(Product.class);
        Media media = Media.builder().imageLink("image.jpg").product(product).build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mediaRepository.findByProductId(productId)).thenReturn(List.of());
        when(mediaRepository.save(any(Media.class))).thenReturn(media);

        MediaDTO result = mediaService.createProductImage(productId, mediaDTO);

        assertEquals(mediaDTO.getImageLink(), result.getImageLink());
    }

    @Test
    void getProductImages_WhenCalled_ThenReturnMediaDTOs() {
        Long productId = 1L;
        List<Media> medias = List.of(new Media(), new Media());
        List<MediaDTO> mediaDTOs = List.of(new MediaDTO(), new MediaDTO());

        when(mediaRepository.findByProductId(productId)).thenReturn(medias);
        when(mediaMapper.toDTOs(medias)).thenReturn(mediaDTOs);

        List<MediaDTO> result = mediaService.getProductImages(productId);

        assertEquals(mediaDTOs, result);
    }

    @Test
    void deleteProductImage_WhenImageNotFound_ThenThrowNotFoundException() {
        Long mediaId = 1L;

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> mediaService.deleteProductImage(mediaId)
        );

        assertEquals(Constants.Message.NOT_FOUND_DATABASE_IMAGES, exception.getMessage());
    }

    @Test
    void deleteProductImage_WhenImageExists_ThenDeleteSuccessfully() {
        Long mediaId = 1L;
        Media media = Media.builder().id(mediaId).build();
        MediaDTO mediaDTO = MediaDTO.builder().id(mediaId).build();

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(mediaMapper.toDTO(media)).thenReturn(mediaDTO);

        MediaDTO result = mediaService.deleteProductImage(mediaId);

        verify(mediaRepository).deleteById(mediaId);
        assertEquals(mediaDTO, result);
    }
}
