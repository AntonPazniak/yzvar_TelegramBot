package com.example.yzvar_telegrambot.services.product;

import com.example.yzvar_telegrambot.dto.product.ProductDTO;
import com.example.yzvar_telegrambot.entities.product.Product;
import com.example.yzvar_telegrambot.mapper.ProductMapper;
import com.example.yzvar_telegrambot.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryProductCache categoryCacheService;
    private final ProductCache productCache;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .weight(productDTO.getWeight())
                .category(categoryCacheService.get(productDTO.getCategory()))
                .active(true)
                .build();
        productRepository.save(product);
        productCache.addOrUpdate(product);
        return productDTO;
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = getProductById(productDTO.getId());
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        productRepository.save(product);
        productCache.addOrUpdate(product);
        return ProductMapper.toDto(product);
    }

    public void updateAvailability(Long productId,Boolean status) {
        var product = getProductById(productId);
        product.setActive(status);
        productRepository.save(product);
        productCache.addOrUpdate(product);
    }

    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findAllByActiveIs(true).stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow();
    }


    @Cacheable(value = "products", key = "#id")
    public Product getProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow();
    }

}
