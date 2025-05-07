package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.dto.order.CreateOrderDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(CreateOrderDTO createOrderDTO);
}
