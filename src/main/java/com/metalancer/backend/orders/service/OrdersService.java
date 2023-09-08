package com.metalancer.backend.orders.service;

import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.users.entity.User;

public interface OrdersService {

    CreatedOrder createOrder(User user, CreateOrder dto);
}