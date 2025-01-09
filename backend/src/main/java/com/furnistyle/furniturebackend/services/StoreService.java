package com.furnistyle.furniturebackend.services;

import java.time.LocalDate;
import java.util.Map;

public interface StoreService {
    double revenueOfCategory(LocalDate startDate, LocalDate endDate, Long categoryId);

    double revenueOfProduct(LocalDate startDate, LocalDate endDate, Long productId);

    double revenueOfStore(LocalDate startDate, LocalDate endDate);

    Map<String, Object> revenueTotalOfYear(int year);

    Map<Long, Double> revenueStatisticsForAllProducts(LocalDate startDate, LocalDate endDate);

    Map<Long, Double> revenueStatisticsForAllCategories(LocalDate startDate, LocalDate endDate);
}
