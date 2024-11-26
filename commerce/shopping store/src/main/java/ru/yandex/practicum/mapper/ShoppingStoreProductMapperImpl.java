package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.entity.ShoppingStoreProduct;

import java.util.UUID;

@Service
public class ShoppingStoreProductMapperImpl implements ShoppingStoreProductMapper {

    public ShoppingStoreProduct toProduct(ProductDto productDto) {
        return ShoppingStoreProduct.builder()
                .productId(UUID.fromString(productDto.productId()))
                .name(productDto.productName())
                .description(productDto.description())
                .imageSrc(productDto.imageSrc())
                .quantityState(productDto.quantityState())
                .productState(productDto.productState())
                .rating(productDto.rating())
                .productCategory(productDto.productCategory())
                .price(productDto.price())
                .build();
    }

    public ProductDto toProductDto(ShoppingStoreProduct shoppingStoreProduct) {
        return ProductDto.builder()
                .productId(shoppingStoreProduct.getProductId().toString())
                .productName(shoppingStoreProduct.getName())
                .description(shoppingStoreProduct.getDescription())
                .imageSrc(shoppingStoreProduct.getImageSrc())
                .quantityState(shoppingStoreProduct.getQuantityState())
                .productState(shoppingStoreProduct.getProductState())
                .rating(shoppingStoreProduct.getRating())
                .productCategory(shoppingStoreProduct.getProductCategory())
                .price(shoppingStoreProduct.getPrice())
                .build();
    }
}