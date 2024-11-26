package ru.yandex.practicum.сontroller;

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
        List<ProductDto> receivedList = shoppingStoreProductService.getAll(category, pageable);
        log.info("Received product list with size {}", receivedList.size());
        return receivedList;
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProductDto = shoppingStoreProductService.create(productDto);
        log.info("Created product {}", productDto);
        return savedProductDto;
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        ProductDto updatedProductDto = shoppingStoreProductService.update(productDto);
        log.info("Updated product {}", productDto);
        return updatedProductDto;
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProduct(@RequestParam String productId) {
        var isProductDeleted = shoppingStoreProductService.isDeleted(productId);
        log.info("Is product removed: {}", isProductDeleted);
        return isProductDeleted;
    }

    @PostMapping("/quantityState")
    public boolean changeProductState(@RequestParam SetProductQuantityStateRequest stateRequest) {
        var isProductChangeState = shoppingStoreProductService.changeState(stateRequest);
        log.info("Is product change state {}", isProductChangeState);
        return isProductChangeState;
    }


    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable String productId) {
        ProductDto receivedProductDto = shoppingStoreProductService.get(productId);
        log.info("Retrieved product with productId: {}", productId);
        return receivedProductDto;
    }
}