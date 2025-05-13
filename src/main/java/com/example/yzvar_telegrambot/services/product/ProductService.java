package com.example.yzvar_telegrambot.services.product;

import com.example.yzvar_telegrambot.dto.product.ProductDTO;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(ProductDTO productDTO);
}
