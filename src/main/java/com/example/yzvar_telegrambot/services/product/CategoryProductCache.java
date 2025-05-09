package com.example.yzvar_telegrambot.services.product;

import com.example.yzvar_telegrambot.entities.product.CategoryProduct;
import com.example.yzvar_telegrambot.enums.CategoryEnum;
import com.example.yzvar_telegrambot.repositories.CategoryProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@DependsOn("categoryProductInitializer")
public class CategoryProductCache {
    private final CategoryProductRepository repository;
    private final Map<CategoryEnum, CategoryProduct> categoryCache = new EnumMap<>(CategoryEnum.class);

    @PostConstruct
    public void initCache() {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            CategoryProduct category = repository.findByName(categoryEnum)
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + categoryEnum));
            categoryCache.put(categoryEnum, category);
        }
    }

    public CategoryProduct get(CategoryEnum categoryEnum) {
        return categoryCache.get(categoryEnum);
    }

    public List<CategoryProduct> getAll() {
        return new ArrayList<>(categoryCache.values());
    }

    public CategoryEnum getCategoryEnumById(Long id) {
        return categoryCache.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(id))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("CategoryEnum not found for id: " + id));
    }



}
