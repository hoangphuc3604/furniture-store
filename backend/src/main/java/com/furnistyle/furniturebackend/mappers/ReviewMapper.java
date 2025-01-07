package com.furnistyle.furniturebackend.mappers;

import com.furnistyle.furniturebackend.dtos.bases.ReviewDTO;
import com.furnistyle.furniturebackend.models.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<Review, ReviewDTO> {
}
