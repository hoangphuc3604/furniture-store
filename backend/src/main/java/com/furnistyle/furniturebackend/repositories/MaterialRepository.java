package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
