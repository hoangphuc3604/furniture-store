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
            .orElseThrow(() -> new NotFoundException("Không tìm thấy chất liệu có id: " + id)));
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialMapper.toDTOs(materialRepository.findAll());
    }

    @Override
    public MaterialDTO updateMaterial(MaterialDTO materialDTO) {
        Material existingMaterial = materialRepository.findById(materialDTO.getId())
            .orElseThrow(() -> new NotFoundException("Không tìm thấy chất liệu!"));
        existingMaterial.setMaterialName(materialDTO.getMaterialName());
        try {
            materialRepository.save(existingMaterial);
        } catch (Exception e) {
            throw new DataAccessException("Không thể cập nhật thông tin chất liệu!");
        }
        return materialMapper.toDTO(existingMaterial);
    }

    @Override
    public MaterialDTO deleteMaterial(long id) throws Exception {
        Material material = materialRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy chất liệu!"));

        List<Product> products = productRepository.findByMaterialId(id);
        if (!products.isEmpty()) {
            throw new IllegalStateException("Không thể xóa chất liệu vì có các phẩm liên quan!");
        } else {
            materialRepository.deleteById(id);
            return materialMapper.toDTO(material);
        }
    }
}
