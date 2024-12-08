package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.List;

public interface ShoppingStoreProductService {

    List<ProductDto> getAll(ProductCategory category, Pageable pageable);

    ProductDto create(ProductDto productDto);

    ProductDto get(String productId);

    ProductDto update(ProductDto productDto);

    boolean isDeleted(String productId);

    boolean changeState(SetProductQuantityStateRequest stateRequest);

}