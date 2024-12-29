package com.furnistyle.furniturebackend.models;

import com.furnistyle.furniturebackend.enums.EOrderStatus;
import com.furnistyle.furniturebackend.exceptions.ErrorConstraintFieldException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_customer", nullable = false)
    private User createdCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_admin")
    private User confirmedAdmin;

    @Enumerated(EnumType.STRING)
    private EOrderStatus status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();

        if (createdCustomer == confirmedAdmin) {
            throw new ErrorConstraintFieldException("nhân viên xác nhận không được xác nhận đơn hàng của mình!");
        }
    }

    @PreUpdate
    protected void checkUpdate() {
        if (createdCustomer == confirmedAdmin) {
            throw new ErrorConstraintFieldException("nhân viên xác nhận không được xác nhận đơn hàng của mình!");
        }
    }

    @Column(nullable = false)
    private String address;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
}
