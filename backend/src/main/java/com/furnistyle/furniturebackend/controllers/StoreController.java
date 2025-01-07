package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.services.StoreService;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/revenueOfCategory")
    ResponseEntity<Double> revenueOfCategory(@RequestParam LocalDate startDate,
                                             @RequestParam LocalDate endDate,
                                             @RequestParam Long categoryId) {
        return ResponseEntity.ok(storeService.revenueOfCategory(startDate, endDate, categoryId));
    }

    @GetMapping("/revenueOfProduct")
    ResponseEntity<Double> revenueOfProduct(@RequestParam LocalDate startDate,
                                            @RequestParam LocalDate endDate,
                                            @RequestParam Long productId) {
        return ResponseEntity.ok(storeService.revenueOfProduct(startDate, endDate, productId));
    }

    @GetMapping("/revenueOfStore")
    ResponseEntity<Double> revenueOfStore(@RequestParam LocalDate startDate,
                                          @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(storeService.revenueOfStore(startDate, endDate));
    }

    @GetMapping("/revenueStatisticsForAllProducts")
    ResponseEntity<Map<Long, Double>> revenueStatisticsForAllProducts(@RequestParam LocalDate startDate,
                                                                      @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(storeService.revenueStatisticsForAllProducts(startDate, endDate));
    }

    @GetMapping("/revenueStatisticsForAllCategories")
    ResponseEntity<Map<Long, Double>> revenueStatisticsForAllCategories(@RequestParam LocalDate startDate,
                                                                        @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(storeService.revenueStatisticsForAllCategories(startDate, endDate));
    }
}
