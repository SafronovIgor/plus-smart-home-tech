package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.ShoppingStoreProduct;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.List;

public interface ShoppingStoreProductRepository extends JpaRepository<ShoppingStoreProduct, String> {

    List<ShoppingStoreProduct> findByProductCategory(ProductCategory category, Pageable pageable);
}