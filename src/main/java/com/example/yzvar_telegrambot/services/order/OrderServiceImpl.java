package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.dto.order.CreateOrderDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.entities.order.Order;
import com.example.yzvar_telegrambot.repositories.OrderRepository;
import com.example.yzvar_telegrambot.services.product.ProductCache;
import com.example.yzvar_telegrambot.services.user.UserCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductCache productCache;
    private final UserCache userCache;

    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = Order.builder()
                .user(userCache.getById(createOrderDTO.getUserId()))
                .product(productCache.getById(createOrderDTO.getProductId()))
                .quantity(createOrderDTO.getQuantity())
                .build();
        orderRepository.save(order);
        return OrderDTO.builder().build();
    }
}
