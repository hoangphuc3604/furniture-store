package com.furnistyle.furniturebackend.controllers;

import com.furnistyle.furniturebackend.dtos.bases.OrderDTO;
import com.furnistyle.furniturebackend.dtos.bases.OrderDetailDTO;
import com.furnistyle.furniturebackend.dtos.requests.OrderRequest;
import com.furnistyle.furniturebackend.dtos.requests.UpdateOrderDetailRequest;
import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.services.OrderDetailService;
import com.furnistyle.furniturebackend.services.OrderService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/getOrderById")
    ResponseEntity<OrderDTO> getOrderById(@RequestParam Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/getOrderDetails")
    ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@RequestParam Long id) {
        return ResponseEntity.ok(orderDetailService.getAllByOrderId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/getAllOrders")
    ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getOrdersOfCurrentUser")
    ResponseEntity<List<OrderDTO>> getOrdersOfCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(orderService.getOrdersOfCurrentUser(token));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/getOrdersByConfirmAdminId")
    ResponseEntity<List<OrderDTO>> getOrdersByConfirmAdmin(@RequestParam Long id) {
        return ResponseEntity.ok(orderService.getOrdersByConfirmAdmin(id));
    }

    @GetMapping("/getOrdersByStatus")
    ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam(required = false) String status) {
        if (status == null) {
            return ResponseEntity.ok(orderService.getAllOrders());
        }
        return ResponseEntity.ok(orderService.getOrdersByStatus(EOrderStatus.valueOf(status)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> request) {
        if (orderService.updateStatus(((Integer) request.get("order_id")).longValue(),
            EOrderStatus.valueOf((String) request.get("status")))) {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_SUCCESSFUL);
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(500))
                .body(Constants.Message.UPDATE_ORDER_FAILED);
        }
    }

    @PostMapping("/admin/updateConfirmAdmin")
    ResponseEntity<String> updateConfirmAdmin(@RequestBody Map<String, Object> request) {
        if (orderService.updateConfirmAdmin(((Integer) request.get("order_id")).longValue(),
            ((Integer) request.get("admin_id")).longValue())) {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_FAILED);
        }
    }

    @PostMapping("/updateAddress")
    ResponseEntity<String> updateAddress(@RequestBody Map<String, Object> request) {
        if (orderService.updateAddress(((Integer) request.get("order_id")).longValue(),
            (String) request.get("address"))) {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_FAILED);
        }
    }

    @PostMapping("/updateOrderDetails")
    ResponseEntity<String> updateOrderDetails(@RequestBody UpdateOrderDetailRequest updateOrderDetailRequest) {
        if (orderService.updateOrderDetails(updateOrderDetailRequest.getOrderId(),
            updateOrderDetailRequest.getOrderDetailDTOS())) {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.UPDATE_ORDER_FAILED);
        }
    }

    @PostMapping("/createOrder")
    ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        if (orderService.createOrder(orderRequest)) {
            return ResponseEntity.ok(Constants.Message.CREATE_ORDER_SUCCESSFUL);
        } else {
            return ResponseEntity.ok(Constants.Message.CREATE_ORDER_FAILED);
        }
    }
}