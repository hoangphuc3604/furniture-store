package com.furnistyle.furniturebackend.services.impl;

import com.furnistyle.furniturebackend.dtos.bases.OrderDTO;
import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.dtos.requests.OrderRequest;
import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.enums.ERole;
import com.furnistyle.furniturebackend.exceptions.BadRequestException;
import com.furnistyle.furniturebackend.exceptions.DataAccessException;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.exceptions.UnauthorizedException;
import com.furnistyle.furniturebackend.mappers.OrderDetailMapper;
import com.furnistyle.furniturebackend.mappers.OrderMapper;
import com.furnistyle.furniturebackend.models.Order;
import com.furnistyle.furniturebackend.models.OrderDetail;
import com.furnistyle.furniturebackend.models.User;
import com.furnistyle.furniturebackend.models.embeddedid.OrderDetailId;
import com.furnistyle.furniturebackend.repositories.OrderDetailRepository;
import com.furnistyle.furniturebackend.repositories.OrderRepository;
import com.furnistyle.furniturebackend.repositories.ProductRepository;
import com.furnistyle.furniturebackend.repositories.UserRepository;
import com.furnistyle.furniturebackend.services.OrderDetailService;
import com.furnistyle.furniturebackend.services.OrderService;
import com.furnistyle.furniturebackend.utils.Constants;
import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderMapper.toDTO((orderRepository.findById(id).orElseThrow(() -> new NotFoundException(
            Constants.Message.NOT_FOUND_ORDER))));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long id) {
        return orderMapper.toDTOs(orderRepository.findByCreatedCustomerId(id));
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(EOrderStatus status) {
        return orderMapper.toDTOs(orderRepository.findByStatus(status));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(OrderRequest createOrderRequest) {
        Order order = new Order();
        order.setCreatedCustomer(userRepository.findById(createOrderRequest.getCreatedCustomerId())
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER)));
        order.setStatus(EOrderStatus.PENDING);
        order.setAddress(createOrderRequest.getAddress());
        order.setCreatedDate(LocalDateTime.now());
        double total = createOrderRequest.getOrderDetailDTOs().stream()
            .mapToDouble(item -> item.getAmount() * item.getPrice()).sum();
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : createOrderRequest.getOrderDetailDTOs()) {
            OrderDetail apply = orderDetailMapper.toEntity(orderDetailDTO);
            apply.setOrderDetailId(new OrderDetailId(order.getId(), orderDetailDTO.getProductId()));
            apply.setOrder(order);
            apply.setProduct(productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_PRODUCT)));
            orderDetails.add(apply);
        }
        orderDetailRepository.saveAll(orderDetails);

        return true;
    }

    @Override
    public boolean updateStatus(Long orderId, EOrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
            new NotFoundException(Constants.Message.NOT_FOUND_ORDER)
        );

        order.setStatus(status);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new DataAccessException(Constants.Message.FAILED_WHILE_SAVING_ORDER);
        }

        return true;
    }

    @Override
    public boolean updateConfirmAdmin(Long orderId, Long adminId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
            new NotFoundException(Constants.Message.NOT_FOUND_ORDER)
        );

        if (order.getConfirmedAdmin() != null) {
            throw new BadRequestException(Constants.Message.ORDER_IS_CONFIRMED);
        }

        if (Objects.equals(order.getCreatedCustomer().getId(), adminId)) {
            throw new ValidationException(Constants.Message.DUPLICATE_CREATED_CONFIRMED_PERSION);
        }

        User admin = userRepository.findById(adminId).orElseThrow(()
            -> new NotFoundException(Constants.Message.NOT_FOUND_USER));

        if (admin.getRole() != ERole.ADMIN || admin.getRole() != ERole.SUPER_ADMIN) {
            throw new UnauthorizedException(Constants.Message.UNAUTHORIZED);
        }

        order.setConfirmedAdmin(admin);
        try {
            orderRepository.save(order);
        } catch (Exception e) {

            throw new DataAccessException(Constants.Message.FAILED_WHILE_SAVING_ORDER);
        }

        return true;
    }

    @Override
    public boolean updateAddress(Long orderId, String address) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
            new NotFoundException(Constants.Message.NOT_FOUND_ORDER)
        );

        order.setAddress(address);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new DataAccessException(Constants.Message.FAILED_WHILE_SAVING_ORDER);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderDetails(Long orderId, List<OrderDetailDTO> orderDetailDTOS) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(Constants.Message.NOT_FOUND_ORDER);
        }

        List<OrderDetailDTO> curList = orderDetailMapper.toDTOs(orderDetailRepository.findByOrderId(orderId));

        Set<Long> existingIds = new HashSet<>();
        for (OrderDetailDTO item : curList) {
            existingIds.add(item.getProductId());
        }

        Set<Long> requestIds = new HashSet<>();
        for (OrderDetailDTO item : orderDetailDTOS) {
            requestIds.add(item.getProductId());
        }

        List<OrderDetailDTO> deleteList = curList.stream()
            .filter(item -> !requestIds.contains(item.getProductId())).toList();

        List<OrderDetailDTO> addList = orderDetailDTOS.stream()
            .filter(item -> !existingIds.contains(item.getProductId())).toList();

        List<OrderDetailDTO> updateList = orderDetailDTOS.stream()
            .filter(item -> existingIds.contains(item.getProductId())).toList();

        if (!deleteList.isEmpty()) {
            deleteList.forEach(item -> orderDetailService.deleteOrderDetail(orderId, item.getProductId()));
        }
        if (!updateList.isEmpty()) {
            updateList.forEach(item -> {
                item.setOrderId(orderId);
                orderDetailService.updateOrderDetail(item);
            });
        }
        if (!addList.isEmpty()) {
            addList.forEach(item -> {
                item.setOrderId(orderId);
                orderDetailService.createOrderDetail(item);
            });
        }

        return true;
    }
}