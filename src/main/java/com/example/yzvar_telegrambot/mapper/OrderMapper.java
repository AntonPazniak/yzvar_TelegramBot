package com.example.yzvar_telegrambot.mapper;

import com.example.yzvar_telegrambot.dto.order.OrderDTO;
import com.example.yzvar_telegrambot.entities.order.Order;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getCreatedDate())
                .title(order.getProduct().getTitle())
                .price(order.getPrice())
                .orderStatus(order.getStatusOrder().getName())
                .build();
    }

}
