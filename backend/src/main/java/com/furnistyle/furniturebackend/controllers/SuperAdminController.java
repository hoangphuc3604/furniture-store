package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.services.SuperAdminService;
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
public class SuperAdminController {
    private final SuperAdminService superAdminService;

    @GetMapping("/revenueOfCategory")
    ResponseEntity<Double> revenueOfCategory(@RequestParam LocalDate startDate,
                                             @RequestParam LocalDate endDate,
                                             @RequestParam Long categoryId) {
        return ResponseEntity.ok(superAdminService.revenueOfCategory(startDate, endDate, categoryId));
    }

    @GetMapping("/revenueOfProduct")
    ResponseEntity<Double> revenueOfProduct(@RequestParam LocalDate startDate,
                                            @RequestParam LocalDate endDate,
                                            @RequestParam Long productId) {
        return ResponseEntity.ok(superAdminService.revenueOfProduct(startDate, endDate, productId));
    }

    @GetMapping("/revenueOfStore")
    ResponseEntity<Double> revenueOfStore(@RequestParam LocalDate startDate,
                                          @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(superAdminService.revenueOfStore(startDate, endDate));
    }

    @GetMapping("/revenueStatisticsForAllProducts")
    ResponseEntity<Map<Long, Double>> revenueStatisticsForAllProducts(@RequestParam LocalDate startDate,
                                                                      @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(superAdminService.revenueStatisticsForAllProducts(startDate, endDate));
    }

    @GetMapping("/revenueStatisticsForAllCategories")
    ResponseEntity<Map<Long, Double>> revenueStatisticsForAllCategories(@RequestParam LocalDate startDate,
                                                                        @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(superAdminService.revenueStatisticsForAllCategories(startDate, endDate));
    }
}
