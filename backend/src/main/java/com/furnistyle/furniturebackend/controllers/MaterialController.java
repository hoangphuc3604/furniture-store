package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.MaterialDTO;
import com.furnistyle.furniturebackend.services.MaterialService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping("")
    public ResponseEntity<String> createMaterial(@Valid @RequestBody MaterialDTO materialDTO) {
        materialService.createMaterial(materialDTO);
        return ResponseEntity.ok(Constants.Message.ADD_MATERIAL_SUCCESSFUL);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable("id") Long materialId) {
        return ResponseEntity.ok(materialService.getMaterialById(materialId));
    }

    @GetMapping("")
    public ResponseEntity<List<MaterialDTO>> getAllCategories() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @PutMapping("")
    public ResponseEntity<String> updateMaterial(@Valid @RequestBody MaterialDTO materialDTO) {
        materialService.updateMaterial(materialDTO);
        return ResponseEntity.ok(Constants.Message.UPDATE_MATERIAL_SUCCESSFUL);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaterialById(@PathVariable("id") Long materialId) {
        try {
            materialService.deleteMaterial(materialId);
            return ResponseEntity.ok(Constants.Message.DELETE_MATERIAL_SUCCESSFUL);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
