package com.example.yzvar_telegrambot.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreateDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
