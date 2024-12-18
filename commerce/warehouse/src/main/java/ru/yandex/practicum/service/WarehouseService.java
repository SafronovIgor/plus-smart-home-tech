package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest newProduct);

    void returnProducts(Map<UUID, Long> products);

    BookedProductsDto bookProducts(ShoppingCartDto shoppingCart);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductForOrderFromShoppingCartRequest assembly);

    void addingProductsQuantity(AddProductToWarehouseRequest addingProductsQuantity);

    AddressDto getWarehouseAddress();
}