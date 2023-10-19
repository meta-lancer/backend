package com.metalancer.backend.users.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import lombok.Getter;


@Getter
public class OrderStatusList {

    private String name;
    private String korName;

    public OrderStatusList(OrderStatus orderStatus) {
        this.name = orderStatus.toString();
        this.korName = orderStatus.getKorName();
    }
}
