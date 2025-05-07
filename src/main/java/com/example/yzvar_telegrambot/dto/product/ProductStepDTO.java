package com.example.yzvar_telegrambot.dto.product;

import com.example.yzvar_telegrambot.enums.ProductStepEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStepDTO {
    private Long chatId;
    private ProductStepEnum productStep;
    private ProductDTO productDTO;
}
