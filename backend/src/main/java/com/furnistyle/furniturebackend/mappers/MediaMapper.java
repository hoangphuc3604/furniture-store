package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.MediaDTO;
import com.furnistyle.furniturebackend.models.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<Media, MediaDTO> {
    @Override
    @Mapping(source = "product.id", target = "productId")
    MediaDTO toDTO(Media media);

    @Override
    @Mapping(source = "productId", target = "product.id")
    Media toEntity(MediaDTO mediaDTO);
}
