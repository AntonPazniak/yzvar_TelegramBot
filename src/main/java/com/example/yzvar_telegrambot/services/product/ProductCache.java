package com.example.yzvar_telegrambot.services.product;

import com.example.yzvar_telegrambot.entities.product.Product;
import com.example.yzvar_telegrambot.repositories.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCache {

    private final ProductRepository productRepository;
    private final Map<Long, Product> productCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void initCache() {
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            productCache.put(product.getId(), product);
        }
    }

    public Product getById(Long id) {
        Product product = productCache.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product with id " + id + " not found in cache");
        }
        return product;
    }

    public Boolean isExist(Long id) {
        return productCache.containsKey(id);
    }

    public Collection<Product> getAll() {
        return productCache.values();
    }

    public Collection<Product> getAllActiveProducts() {
        return productCache.values().stream().filter(Product::getActive).collect(Collectors.toList());
    }

    public void refresh() {
        productCache.clear();
        initCache();
    }

    public void addOrUpdate(Product product) {
        productCache.put(product.getId(), product);
    }

    public void remove(Long id) {
        productCache.remove(id);
    }

}