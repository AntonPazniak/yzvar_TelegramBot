package com.example.yzvar_telegrambot.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
