package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.furnistyle.furniturebackend.dtos.bases.MaterialDTO;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.MaterialMapper;
import com.furnistyle.furniturebackend.models.Material;
import com.furnistyle.furniturebackend.repositories.MaterialRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.services.impl.MaterialServiceImpl;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MaterialServiceTest {

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialServiceImpl materialService;

    @Test
    void createMaterial_WhenValidMaterialDTO_ThenMaterialCreatedSuccessfully() {
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("Wood");

        Material material = new Material();
        material.setId(1L);
        material.setMaterialName("Wood");

        Mockito.when(materialMapper.toEntity(materialDTO)).thenReturn(material);
        Mockito.when(materialRepository.save(material)).thenReturn(material);
        Mockito.when(materialMapper.toDTO(material)).thenReturn(materialDTO);

        MaterialDTO result = materialService.createMaterial(materialDTO);

        assertEquals(materialDTO.getMaterialName(), result.getMaterialName());
        Mockito.verify(materialRepository).save(material);
    }

    @Test
    void getMaterialById_WhenMaterialNotFound_ThenThrowNotFoundException() {
        Long materialId = 1L;

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> materialService.getMaterialById(materialId)
        );

        assertEquals(Constants.Message.NOT_FOUND_MATERIAL, exception.getMessage());
    }

    @Test
    void getMaterialById_WhenMaterialExists_ThenReturnMaterialDTO() {
        Long materialId = 1L;
        Material material = new Material();
        material.setId(materialId);
        material.setMaterialName("Wood");

        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("Wood");

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));
        Mockito.when(materialMapper.toDTO(material)).thenReturn(materialDTO);

        MaterialDTO result = materialService.getMaterialById(materialId);

        assertEquals(materialDTO.getMaterialName(), result.getMaterialName());
    }

    @Test
    void updateMaterial_WhenMaterialNotFound_ThenThrowNotFoundException() {
        Long materialId = 1L;
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setId(materialId);
        materialDTO.setMaterialName("Updated Material");

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> materialService.updateMaterial(materialDTO)
        );

        assertEquals(Constants.Message.NOT_FOUND_MATERIAL, exception.getMessage());
    }

    @Test
    void updateMaterial_WhenMaterialExists_ThenUpdateMaterialSuccessfully() {
        Long materialId = 1L;
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setId(materialId);
        materialDTO.setMaterialName("Updated Material");

        Material material = new Material();
        material.setId(materialId);
        material.setMaterialName("Wood");

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));
        Mockito.when(materialRepository.save(material)).thenReturn(material);
        Mockito.when(materialMapper.toDTO(material)).thenReturn(materialDTO);

        MaterialDTO result = materialService.updateMaterial(materialDTO);

        assertEquals("Updated Material", result.getMaterialName());
        Mockito.verify(materialRepository).save(material);
    }

    @Test
    void deleteMaterial_WhenMaterialNotFound_ThenThrowNotFoundException() {
        Long materialId = 1L;

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> materialService.deleteMaterial(materialId)
        );

        assertEquals(Constants.Message.NOT_FOUND_MATERIAL, exception.getMessage());
    }

    @Test
    void deleteMaterial_WhenMaterialExists_ThenDeleteMaterialSuccessfully() throws Exception {
        Long materialId = 1L;
        Material material = new Material();
        material.setId(materialId);
        material.setMaterialName("Wood");

        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setId(materialId);
        materialDTO.setMaterialName("Wood");

        Mockito.when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));
        Mockito.when(productRepository.findByMaterialId(materialId)).thenReturn(Collections.emptyList());
        Mockito.when(materialMapper.toDTO(material)).thenReturn(materialDTO);

        MaterialDTO result = materialService.deleteMaterial(materialId);

        assertNotNull(result);
        assertEquals(materialId, result.getId());
        assertEquals("Wood", result.getMaterialName());

        Mockito.verify(materialRepository).deleteById(materialId);
    }

    @Test
    void getAllMaterials_WhenMaterialsExist_ThenReturnListOfMaterialDTOs() {
        List<Material> materials = List.of(
            new Material(1L, "Wood", null),
            new Material(2L, "Metal", null)
        );
        List<MaterialDTO> materialDTOs = List.of(
            new MaterialDTO(1L, "Wood"),
            new MaterialDTO(2L, "Metal")
        );

        Mockito.when(materialRepository.findAll()).thenReturn(materials);
        Mockito.when(materialMapper.toDTOs(materials)).thenReturn(materialDTOs);

        List<MaterialDTO> result = materialService.getAllMaterials();

        assertEquals(materialDTOs.size(), result.size());
        assertEquals(materialDTOs.get(0).getMaterialName(), result.get(0).getMaterialName());
    }
}

