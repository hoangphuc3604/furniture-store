package com.furnistyle.furniturebackend.services;

import com.furnistyle.furniturebackend.dtos.bases.CartDetailDTO;
import java.util.List;

public interface CartService {
    List<CartDetailDTO> cartOfCurrentUser(String token);

    void addToCart(String token, Long productId, Integer amount);

    void updateCartDetail(String token, Long productId, Integer amount);

    void deleteCartDetail(String token, Long productId);
}
