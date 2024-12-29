package com.furnistyle.furniturebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    @GetMapping("")
    public ResponseEntity<String> getProducts() {
        return ResponseEntity.ok("GET products successfully!");
    }
}
