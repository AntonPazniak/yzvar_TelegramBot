package com.example.yzvar_telegrambot.mapper;

import com.example.yzvar_telegrambot.dto.product.ProductDTO;
import com.example.yzvar_telegrambot.entities.product.Product;

public class ProductMapper {

    public static ProductDTO toDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .build();
    }

}
