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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/getAllOrders")
    ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getOrdersByUserId")
    ResponseEntity<List<OrderDTO>> getOrdersByUserId(@RequestParam Long id) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(id));
    }

    @GetMapping("/getOrdersByStatus")
    ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(EOrderStatus.valueOf(status)));
    }

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

    @PostMapping("/updateConfirmAdmin")
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