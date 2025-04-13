package com.example.yzvar_telegrambot.componets.initializers;


import com.example.yzvar_telegrambot.entities.product.CategoryProduct;
import com.example.yzvar_telegrambot.enums.CategoryEnum;
import com.example.yzvar_telegrambot.repositories.CategoryProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("categoryProductInitializer")
@RequiredArgsConstructor
public class CategoryProductInitializer {

    private final CategoryProductRepository productRepository;

    @PostConstruct
    public void initRoles() {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            productRepository.findByName(categoryEnum).orElseGet(() -> {
                CategoryProduct role = CategoryProduct.builder().name(categoryEnum).build();
                return productRepository.save(role);
            });
        }
    }

}
