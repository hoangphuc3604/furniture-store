package com.furnistyle.furniturebackend.dtos.bases;

import com.furnistyle.furniturebackend.enums.EOrderStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderDTO {
    
    private Long id;
    private UserDTO createdCustomer;
    private UserDTO confirmedAdmin;
    private EOrderStatus status;
    private LocalDateTime createdDate;
    private String address;
    private Double totalAmount;
}
