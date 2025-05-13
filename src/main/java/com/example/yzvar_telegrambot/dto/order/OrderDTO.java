package com.example.yzvar_telegrambot.dto.order;

import com.example.yzvar_telegrambot.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String title;
    private Float price;
    private OrderStatusEnum orderStatus;

}
