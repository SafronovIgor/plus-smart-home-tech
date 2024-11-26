package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.entity.ShoppingStoreProduct;

public interface ShoppingStoreProductMapper {

    ProductDto toProductDto(ShoppingStoreProduct shoppingStoreProduct);

    ShoppingStoreProduct toProduct(ProductDto productDto);
}