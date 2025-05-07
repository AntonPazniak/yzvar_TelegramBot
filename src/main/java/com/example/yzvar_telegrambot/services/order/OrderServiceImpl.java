package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.dto.order.CreateOrderDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.entities.order.Order;
import com.example.yzvar_telegrambot.enums.StatusOrderEnum;
import com.example.yzvar_telegrambot.mapper.OrderMapper;
import com.example.yzvar_telegrambot.repositories.OrderRepository;
import com.example.yzvar_telegrambot.services.product.ProductCache;
import com.example.yzvar_telegrambot.services.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductCache productCache;
    private final StatusOrderCache statusOrderCache;
    private final UserServiceImpl userService;

    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = Order.builder()
                .user(userService.getUserByIdOrElseThrow(createOrderDTO.getUserId()))
                .product(productCache.getById(createOrderDTO.getProductId()))
                .statusOrder(statusOrderCache.get(StatusOrderEnum.IN_PROGRESS))
                .quantity(createOrderDTO.getQuantity())
                .build();
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }

    public OrderDTO updateStatusOrder(Long orderId, StatusOrderEnum statusOrderEnum) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatusOrder(statusOrderCache.get(statusOrderEnum));
        orderRepository.save(order);
        return OrderDTO.builder().build();
    }


}
