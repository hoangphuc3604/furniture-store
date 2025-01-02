package com.furnistyle.furniturebackend.repositories;

import com.furnistyle.furniturebackend.models.CartDetail;
import com.furnistyle.furniturebackend.models.embeddedid.CartDetailId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartDetail, CartDetailId> {
    List<CartDetail> findAllByOwner_Id(Long ownerId);
}
