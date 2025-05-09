package com.example.yzvar_telegrambot.dto.product;

import com.example.yzvar_telegrambot.enums.ProductEditStepEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductEditStepDTO {
    private ProductEditStepEnum productEditStep;
    private ProductDTO productDTO;
}
