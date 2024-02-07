package com.metalancer.backend.orders.service;

import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.domain.PaymentResponse;
import com.metalancer.backend.orders.dto.OrdersRequestDTO;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CancelAllPayment;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrderWebhook;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateFreeOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.users.entity.User;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.io.IOException;

public interface OrdersService {

    CreatedOrder createOrder(User user, CreateOrder dto);

    PaymentResponse completePayment(User user, CompleteOrder dto)
        throws IamportResponseException, IOException;

    PaymentResponse completePaymentByWebhook(CompleteOrderWebhook dto)
        throws IamportResponseException, IOException;

    boolean checkPayment(OrdersRequestDTO.CheckPayment dto)
        throws IamportResponseException, IOException;

    PaymentResponse cancelAllPayment(User user, CancelAllPayment dto)
        throws IamportResponseException, IOException;

    CreatedOrder createOrderByEn(User user, CreateOrder dto);

    PaymentResponse createFreeOrder(User user, CreateFreeOrder dto);
}