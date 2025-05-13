package com.example.yzvar_telegrambot.services.order;

import com.example.yzvar_telegrambot.dto.order.OrderCreateDTO;
import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.entities.order.Order;
import com.example.yzvar_telegrambot.entities.product.Product;
import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import com.example.yzvar_telegrambot.mapper.OrderMapper;
import com.example.yzvar_telegrambot.repositories.OrderRepository;
import com.example.yzvar_telegrambot.services.product.ProductCache;
import com.example.yzvar_telegrambot.services.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductCache productCache;
    private final StatusOrderCache statusOrderCache;
    private final UserServiceImpl userService;

    public OrderDTO createOrder(OrderCreateDTO createOrderDTO) {
        Product product = productCache.getById(createOrderDTO.getProductId());
        Order order = Order.builder()
                .user(userService.getUserByIdOrElseThrow(createOrderDTO.getUserId()))
                .product(product)
                .statusOrder(statusOrderCache.get(OrderStatusEnum.PROCESSING))
                .quantity(createOrderDTO.getQuantity())
                .price(product.getPrice() * createOrderDTO.getQuantity())
                .build();
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }

    public OrderDTO updateStatusOrder(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId()).orElseThrow();
        order.setStatusOrder(statusOrderCache.get(orderDTO.getOrderStatus()));
        orderRepository.save(order);
        return OrderDTO.builder().build();
    }

    public OrderDTO updateOrderPrice(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId()).orElseThrow();
        order.setPrice(orderDTO.getPrice());
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderMapper.toDTO(order);
    }

    public List<OrderDTO> getAllOrdersByStatus(OrderStatusEnum statusOrderEnum) {
        return orderRepository.findAllByStatusOrder(statusOrderCache.get(statusOrderEnum))
                .stream().map(OrderMapper::toDTO).toList();
    }

    public List<OrderDTO> getAllOrdersByStatusAndUsername(OrderStatusEnum statusOrderEnum, String username) {
        return orderRepository.findAllByStatusOrderAndUser_Username(statusOrderCache.get(statusOrderEnum), username)
                .stream().map(OrderMapper::toDTO).toList();
    }

    public List<OrderDTO> getAllOrdersByIdUserAndStatus(Long userId, OrderStatusEnum statusOrderEnum) {
        return orderRepository.findAllByUser_idAndStatusOrder_Name(userId, statusOrderEnum)
                .stream().map(OrderMapper::toDTO).toList();
    }


}
