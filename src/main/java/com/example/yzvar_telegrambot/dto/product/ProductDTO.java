package com.example.yzvar_telegrambot.dto.product;

import com.example.yzvar_telegrambot.enums.CategoryEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
        private Long id;
        private String title;
        private String description;
        private Float price;
        private CategoryEnum category;
        private Boolean status;
        private Integer weight;
}
