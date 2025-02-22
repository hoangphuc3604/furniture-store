package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.OrderDTO;
import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.mappers.OrderDetailMapper;
import com.furnistyle.furniturebackend.mappers.OrderMapper;
import com.furnistyle.furniturebackend.models.Product;
import com.furnistyle.furniturebackend.repositories.OrderDetailRepository;
import com.furnistyle.furniturebackend.repositories.OrderRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.StoreService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final OrderMapper orderMapper;

    @Override
    public double revenueOfCategory(LocalDate startDate, LocalDate endDate, Long categoryId) {
        List<OrderDTO> orderDTOS = filterOrdersByDate(startDate, endDate);
        return calculateRevenueByCategory(orderDTOS, categoryId);
    }

    @Override
    public double revenueOfProduct(LocalDate startDate, LocalDate endDate, Long productId) {
        List<OrderDTO> orderDTOS = filterOrdersByDate(startDate, endDate);
        return calculateRevenueByProduct(orderDTOS, productId);
    }

    @Override
    public double revenueOfStore(LocalDate startDate, LocalDate endDate) {
        return filterOrdersByDate(startDate, endDate).stream().mapToDouble(OrderDTO::getTotalAmount).sum();
    }

    @Override
    public Map<String, Object> revenueTotalOfYear(int year) {
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();

        double totalSales = orderRepository.findByStatus(EOrderStatus.DELIVERED).stream()
            .mapToDouble(order -> orderMapper.toDTO(order).getTotalAmount()).sum();

        long totalProducts = productRepository.count();

        Map<String, Object> chartData = generateChartDataForYear(year);

        Map<String, Object> revenueStats = new HashMap<>();
        revenueStats.put("totalUsers", totalUsers);
        revenueStats.put("totalOrders", totalOrders);
        revenueStats.put("totalSales", totalSales);
        revenueStats.put("totalProducts", totalProducts);
        revenueStats.put("chartData", chartData);

        return revenueStats;
    }

    @Override
    public Map<Long, Double> revenueStatisticsForAllProducts(LocalDate startDate, LocalDate endDate) {
        List<OrderDTO> orderDTOS = filterOrdersByDate(startDate, endDate);
        Map<Long, Double> productRevenueMap = new HashMap<>();

        orderDTOS.stream()
            .flatMap(order -> orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(order.getId())).stream())
            .forEach(detail -> productRevenueMap.merge(detail.getProductId(), detail.getPrice() * detail.getAmount(),
                Double::sum));

        return productRevenueMap;
    }

    @Override
    public Map<Long, Double> revenueStatisticsForAllCategories(LocalDate startDate, LocalDate endDate) {
        List<OrderDTO> orderDTOS = filterOrdersByDate(startDate, endDate);
        Map<Long, Double> categoryRevenueMap = new HashMap<>();

        orderDTOS.stream()
            .flatMap(order -> orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(order.getId())).stream())
            .forEach(detail -> {
                Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));

                Long categoryId = product.getCategory().getId();
                double revenue = detail.getPrice() * detail.getAmount();

                categoryRevenueMap.merge(categoryId, revenue, Double::sum);
            });

        return categoryRevenueMap;
    }

    private List<OrderDTO> filterOrdersByDate(LocalDate startDate, LocalDate endDate) {
        return orderMapper.toDTOs(orderRepository.findByStatus(EOrderStatus.DELIVERED)).stream().filter(
            order -> !order.getCreatedDate().isBefore(startDate.atStartOfDay())
                && !order.getCreatedDate().isAfter(endDate.atStartOfDay())).toList();
    }

    private double calculateRevenueByCategory(List<OrderDTO> orderDTOS, Long categoryId) {
        return orderDTOS.stream()
            .flatMap(order -> orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(order.getId())).stream())
            .filter(detail -> {
                Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT));
                return Objects.equals(product.getCategory().getId(), categoryId);
            }).mapToDouble(detail -> detail.getPrice() * detail.getAmount()).sum();
    }

    private double calculateRevenueByProduct(List<OrderDTO> orderDTOS, Long productId) {
        return orderDTOS.stream()
            .flatMap(order -> orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(order.getId())).stream())
            .filter(detail -> Objects.equals(detail.getProductId(), productId))
            .mapToDouble(detail -> detail.getPrice() * detail.getAmount()).sum();
    }

    private Map<String, Object> generateChartDataForYear(int year) {
        List<String> labels = List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        double[] data = new double[12];

        List<OrderDTO> orders = orderMapper.toDTOs(
                orderRepository.findByStatus(EOrderStatus.DELIVERED)).stream()
            .filter(order -> order.getCreatedDate().getYear() == year)
            .toList();

        for (OrderDTO order : orders) {
            int month = order.getCreatedDate().getMonthValue() - 1;
            data[month] += order.getTotalAmount();
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("data", data);

        return chartData;
    }
}
