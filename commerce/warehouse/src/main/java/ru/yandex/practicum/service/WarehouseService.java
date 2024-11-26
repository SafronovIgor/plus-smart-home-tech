package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest newProduct);

    void returnProducts(Map<String, Long> products);

    BookedProductsDto bookProducts(ShoppingCartDto shoppingCart);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductForOrderFromShoppingCartRequest assembly);

    void addingProductsQuantity(AddProductToWarehouseRequest addingProductsQuantity);

    AddressDto getWarehouseAddress();
}