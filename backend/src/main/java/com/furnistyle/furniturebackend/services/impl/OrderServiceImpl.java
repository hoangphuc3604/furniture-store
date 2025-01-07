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
import com.furnistyle.furniturebackend.services.MailService;
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
    private final MailService mailService;

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderMapper.toDTO(
            (orderRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_ORDER))));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long id) {
        User user =
            userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        return orderMapper.toDTOs(orderRepository.findByCreatedCustomerId(id));
    }

    @Override
    public List<OrderDTO> getOrdersByConfirmAdmin(Long id) {
        User admin =
            userRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_USER));
        return orderMapper.toDTOs(orderRepository.findByConfirmedAdmin(admin));
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
        double total =
            createOrderRequest.getOrderDetailDTOs().stream().mapToDouble(item -> item.getAmount() * item.getPrice())
                .sum();
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
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(Constants.Message.NOT_FOUND_ORDER));

        if (!checkStatusBeforeUpdate(order.getStatus(), status)) {
            throw new BadRequestException(Constants.Message.INVALID_STATUS);
        }

        order.setStatus(status);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new DataAccessException(Constants.Message.FAILED_WHILE_SAVING_ORDER);
        }

        String stt = "";
        switch (status) {
            case PENDING -> stt = " đang được xác nhận";
            case PROCESSING -> stt = " đang được xử lí";
            case SHIPPED -> stt = " đang được vận chuyển";
            case DELIVERED -> stt = " đã giao hàng thành công";
            case CANCELLED -> stt = " đã bị hủy";
            default -> stt = "";
        }

        String body = """
                          <!DOCTYPE html>
                          <html lang="vi">
                          <head>
                              <meta charset="UTF-8">
                              <meta name="viewport" content="width=device-width, initial-scale=1.0">
                              <title>Trạng thái đơn hàng</title>
                              <style>
                                  body {
                                      font-family: Arial, sans-serif;
                                      line-height: 1.6;
                                      margin: 0;
                                      padding: 0;
                                      background-color: #f9f9f9;
                                      color: #333;
                                  }
                                  .email-container {
                                      max-width: 600px;
                                      margin: 20px auto;
                                      background-color: #fff;
                                      border: 1px solid #ddd;
                                      border-radius: 5px;
                                      overflow: hidden;
                                      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                                  }
                                  .header {
                                      background-color: #4CAF50;
                                      color: #fff;
                                      text-align: center;
                                      padding: 15px;
                                  }
                                  .header h1 {
                                      margin: 0;
                                      font-size: 24px;
                                  }
                                  .content {
                                      padding: 20px;
                                  }
                                  .content p {
                                      margin: 10px 0;
                                  }
                                  .footer {
                                      text-align: center;
                                      padding: 10px;
                                      background-color: #f1f1f1;
                                      font-size: 14px;
                                      color: #777;
                                  }
                                  .footer a {
                                      color: #4CAF50;
                                      text-decoration: none;
                                  }
                              </style>
                          </head>
                          <body>
                              <div class="email-container">
                                  <div class="header">
                                      <h1>TRẠNG THÁI ĐƠN HÀNG SỐ""" + " " + orderId + """
                          </h1>
                          </div>
                          <div class="content">
                              <p>Xin chào""" + " " + order.getCreatedCustomer().getFullname() + """
                          ,</p>
                          <p>Đơn hàng của bạn có mã số <strong>""" + orderId + "</strong>" + stt + """
                                       .</p>
                                       <p>Nếu bạn có bất kỳ câu hỏi nào,\s
                                       xin vui lòng liên hệ với chúng tôi qua email hoặc số điện thoại hỗ trợ.</p>
                                       <p>Trân trọng,</p>
                                       <p><strong>Đội ngũ Hỗ trợ</strong></p>
                                   </div>
                                   <div class="footer">
                                       <p>Bạn nhận được email này vì bạn đã đặt hàng tại cửa hàng của chúng tôi.</p>
                                       <p><a href="https://yourwebsite.com">Truy cập website</a> để biết thêm thông tin.</p>
                                   </div>
                               </div>
                           </body>
                           </html>
                          \s""";

        String subject = "TRẠNG THÁI ĐƠN HÀNG SỐ " + orderId;
        mailService.sendEmail(order.getCreatedCustomer().getEmail(), subject, body);

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
        if (order.getStatus() != EOrderStatus.PENDING) {
            throw new BadRequestException(Constants.Message.CAN_NOT_UPDATE_ORDER);
        }
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

    boolean checkStatusBeforeUpdate(EOrderStatus curStatus, EOrderStatus newStatus) {
        if (curStatus == null || newStatus == null) {
            return false;
        }

        if (curStatus == EOrderStatus.CANCELLED || curStatus == EOrderStatus.DELIVERED) {
            return false;
        }

        return newStatus.ordinal() >= curStatus.ordinal();
    }
}