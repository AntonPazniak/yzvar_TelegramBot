package com.example.yzvar_telegrambot.dto.order;

import com.example.yzvar_telegrambot.enums.OrderEditStepEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEditStepDTO {
    private OrderEditStepEnum step;
    private OrderDTO orderDTO;
}
