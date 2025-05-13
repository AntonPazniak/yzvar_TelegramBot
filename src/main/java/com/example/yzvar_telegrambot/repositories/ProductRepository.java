package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    ArrayList<Product> findAllByActiveIs(Boolean active);

}
