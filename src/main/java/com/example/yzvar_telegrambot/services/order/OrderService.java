package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.dto.order.OrderCreateDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long orderId);

    OrderDTO updateOrderPrice(OrderDTO orderDTO);

    OrderDTO createOrder(OrderCreateDTO createOrderDTO);

    OrderDTO updateStatusOrder(OrderDTO orderDTO);

    List<OrderDTO> getAllOrdersByStatus(OrderStatusEnum statusOrderEnum);

    List<OrderDTO> getAllOrdersByStatusAndUsername(OrderStatusEnum statusOrderEnum, String username);
}
