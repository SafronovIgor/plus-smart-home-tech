package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.service.ShoppingStoreProductService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingStoreController {
    ShoppingStoreProductService shoppingStoreProductService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("Fetching products with category: {}, pageable: {}", category, pageable);
        return shoppingStoreProductService.getAll(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        log.info("Creating product: {}", productDto);
        return shoppingStoreProductService.create(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        log.info("Updating product: {}", productDto);
        return shoppingStoreProductService.update(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProduct(@RequestParam String productId) {
        log.info("Removing product with ID: {}", productId);
        return shoppingStoreProductService.isDeleted(productId);
    }

    @PostMapping("/quantityState")
    public boolean changeProductState(@RequestBody SetProductQuantityStateRequest stateRequest) {
        log.info("Changing quantity state for product with request: {}", stateRequest);
        return shoppingStoreProductService.changeState(stateRequest);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable String productId) {
        log.info("Fetching product with ID: {}", productId);
        return shoppingStoreProductService.get(productId);
    }
}