package com.metalancer.backend.orders.service;

import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.User;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final ProductsRepository productsRepository;

    @Override
    public CreatedOrder createOrder(User user, CreateOrder dto) {
        String orderNo = createOrderNo();
        OrdersEntity createdOrdersEntity = OrdersEntity.builder().orderer(user)
            .orderNo(orderNo)
            .totalPrice(dto.getTotalPrice()).build();
        ordersRepository.save(createdOrdersEntity);
        int index = 1;
        for (Long productsId : dto.getProductsId()) {
            ProductsEntity foundProductEntity = productsRepository.findProductById(productsId);
            OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
                .orderer(user)
                .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
                .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index++))
                .build();
            orderProductsRepository.save(createdOrderProductsEntity);
        }
        CreatedOrder response = createdOrdersEntity.toCreatedOrderDomain();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            createdOrdersEntity);
        response.setOrderProductList(orderProductsEntityList);
        return response;
    }

    private String createOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String suffix = String.format("%02d", (int) ((Math.random() * (99))));
        return sdf.format(now) + suffix;
    }
}