package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.CartDetailDTO;
import com.furnistyle.furniturebackend.services.CartService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cartOfCurrentUser")
    ResponseEntity<List<CartDetailDTO>> cartOfCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(cartService.cartOfCurrentUser(token));
    }

    @GetMapping("/addToCart")
    ResponseEntity<String> addToCart(@RequestHeader("Authorization") String authHeader, @RequestParam Integer productId,
                                     @RequestParam Integer amount) {
        String token = authHeader.replace("Bearer ", "");
        cartService.addToCart(token, productId.longValue(), amount);
        return ResponseEntity.ok(Constants.Message.ADD_TO_CART_SUCCESSFUL);
    }

    @GetMapping("/updateCartDetail")
    ResponseEntity<String> updateCartDetail(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam Integer productId,
                                            @RequestParam Integer amount) {
        String token = authHeader.replace("Bearer ", "");
        cartService.updateCartDetail(token, productId.longValue(), amount);
        return ResponseEntity.ok(Constants.Message.UPDATE_AMOUNT_PRODUCT_IN_CART_SUCCESSFUL);
    }

    @GetMapping("/deleteCartDetail")
    ResponseEntity<String> deleteCartDetail(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam Integer productId) {
        String token = authHeader.replace("Bearer ", "");
        cartService.deleteCartDetail(token, productId.longValue());
        return ResponseEntity.ok(Constants.Message.REMOVE_FROM_CART_SUCCESSFUL);
    }
}