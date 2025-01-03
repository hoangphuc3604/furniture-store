package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.MaterialDTO;
import com.furnistyle.furniturebackend.exceptions.DataAccessException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.MaterialMapper;
import com.furnistyle.furniturebackend.models.Material;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.MaterialRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.MaterialService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final ProductRepository productRepository;

    @Override
    public MaterialDTO createMaterial(MaterialDTO materialDTO) {
        return materialMapper.toDTO(materialRepository.save(materialMapper.toEntity(materialDTO)));
    }

    @Override
    public MaterialDTO getMaterialById(long id) {
        return materialMapper.toDTO(materialRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_MATERIAL)));
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialMapper.toDTOs(materialRepository.findAll());
    }

    @Override
    public MaterialDTO updateMaterial(MaterialDTO materialDTO) {
        Material existingMaterial = materialRepository.findById(materialDTO.getId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_MATERIAL));
        existingMaterial.setMaterialName(materialDTO.getMaterialName());
        try {
            materialRepository.save(existingMaterial);
        } catch (Exception e) {
            throw new DataAccessException(Constants.Message.UPDATE_MATERIAL_FAILED);
        }
        return materialMapper.toDTO(existingMaterial);
    }

    @Override
    public MaterialDTO deleteMaterial(long id) throws Exception {
        Material material = materialRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_MATERIAL));

        List<Product> products = productRepository.findByMaterialId(id);
        if (!products.isEmpty()) {
            throw new IllegalStateException(Constants.Message.DELETE_MATERIAL_FAILED);
        } else {
            materialRepository.deleteById(id);
            return materialMapper.toDTO(material);
        }
    }
}
