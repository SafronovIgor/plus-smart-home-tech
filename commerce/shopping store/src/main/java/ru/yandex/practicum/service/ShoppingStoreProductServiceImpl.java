package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.entity.ShoppingStoreProduct;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.exception.ProductAlreadyExistException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ShoppingStoreProductMapper;
import ru.yandex.practicum.repository.ShoppingStoreProductRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingStoreProductServiceImpl implements ShoppingStoreProductService {
    ShoppingStoreProductRepository shoppingStoreProductRepository;
    ShoppingStoreProductMapper shoppingStoreProductMapper;

    @Override
    public List<ProductDto> getAll(ProductCategory category, Pageable pageable) {

        return shoppingStoreProductRepository.findByProductCategory(category, pageable).stream()
                .map(shoppingStoreProductMapper::toProductDto)
                .toList();
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        if (shoppingStoreProductRepository.findById(productDto.productId()).isPresent()) {
            throw new ProductAlreadyExistException(productDto.productId());
        }
        log.info("Product for save: {}", shoppingStoreProductMapper.toProduct(productDto));
        ShoppingStoreProduct shoppingStoreProductSaved = shoppingStoreProductRepository.save(
                shoppingStoreProductMapper.toProduct(productDto));
        log.info("Saved product: {}", shoppingStoreProductSaved);
        return shoppingStoreProductMapper.toProductDto(shoppingStoreProductSaved);
    }

    @Override
    public ProductDto get(String productId) {
        ShoppingStoreProduct shoppingStoreProduct = shoppingStoreProductRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        return shoppingStoreProductMapper.toProductDto(shoppingStoreProduct);

    }

    @Override
    public ProductDto update(ProductDto productDto) {
        shoppingStoreProductRepository.findById(productDto.productId()).
                orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + productDto.productId() + " not found"));
        shoppingStoreProductRepository.deleteById(productDto.productId());
        ShoppingStoreProduct shoppingStoreProductSaved = shoppingStoreProductRepository.save(
                shoppingStoreProductMapper.toProduct(productDto));
        return shoppingStoreProductMapper.toProductDto(shoppingStoreProductSaved);
    }

    @Override
    public boolean isDeleted(String productId) {
        return shoppingStoreProductRepository.existsById(productId);
    }

    @Override
    public boolean changeState(SetProductQuantityStateRequest stateRequest) {
        ShoppingStoreProduct shoppingStoreProductInDb = shoppingStoreProductRepository.findById(stateRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + stateRequest.productId() + " not found"));
        try {
            log.info("Changing state for product: {}", stateRequest.productId());
            shoppingStoreProductInDb.setQuantityState(stateRequest.quantityState());
            return true;
        } catch (DataAccessException e) {
            log.error("Failed to change state for product: {}", stateRequest.productId(), e);
            return false;
        }
    }
}