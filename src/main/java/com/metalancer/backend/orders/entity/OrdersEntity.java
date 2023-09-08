package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
@ToString
public class OrdersEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716209177241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User orderer;
    @Column(nullable = false)
    private String orderNo;
    private Integer totalPoint;
    @Column(nullable = false)
    private Integer totalPrice;
    private Integer totalPaymentPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PAY_ING;

    @Builder
    public OrdersEntity(User orderer, String orderNo, Integer totalPrice) {
        this.orderer = orderer;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
    }

    public CreatedOrder toCreatedOrderDomain() {
        return CreatedOrder.builder().orderId(id).ordererId(orderer.getId()).orderNo(orderNo)
            .totalPrice(totalPrice).orderStatus(orderStatus.getKorName()).build();
    }
}
