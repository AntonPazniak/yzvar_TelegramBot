package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.product.CategoryProduct;
import com.example.yzvar_telegrambot.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
    Optional<CategoryProduct> findByName(CategoryEnum name);
}
