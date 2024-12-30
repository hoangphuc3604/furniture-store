package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.MaterialDTO;
import java.util.List;

public interface MaterialService {
    MaterialDTO createMaterial(MaterialDTO materialDTO);

    MaterialDTO getMaterialById(long id);

    List<MaterialDTO> getAllMaterials();

    MaterialDTO updateMaterial(MaterialDTO materialDTO);

    MaterialDTO deleteMaterial(long id) throws Exception;
}
