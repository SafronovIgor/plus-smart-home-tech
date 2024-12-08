package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseController {
    WarehouseService warehouseService;

    @PutMapping
    public void addNewProduct(@RequestBody NewProductInWarehouseRequest newProduct) {
        log.info("==> PUT /api/v1/warehouse Add new product in warehouse: {}", newProduct);
        warehouseService.addNewProduct(newProduct);
        log.info("||| PUT /api/v1/warehouse. Added new product in warehouse");
    }

    @PostMapping("/return")
    public void returnProducts(Map<String, Long> products) {
        log.info("==> POST /api/v1/warehouse/return. Returning products: {}", products);
        warehouseService.returnProducts(products);
        log.info("||| POST /api/v1/warehouse/return. Products returned");
    }

    @PostMapping("/booking")
    public BookedProductsDto bookProducts(@RequestBody ShoppingCartDto shoppingCart) {
        log.info("==> POST /api/v1/warehouse/booking. Booking products: {}", shoppingCart);
        BookedProductsDto bookedProducts = warehouseService.bookProducts(shoppingCart);
        log.info("<== POST /api/v1/warehouse/booking. Booked products: {}", bookedProducts);
        return bookedProducts;
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductForOrder(AssemblyProductForOrderFromShoppingCartRequest assembly) {
        log.info("==> POST /api/v1/warehouse/assembly. Assembly product for order: {}", assembly);
        BookedProductsDto assemblyProducts = warehouseService.assemblyProductsForOrder(assembly);
        log.info("<== POST /api/v1/warehouse/assembly. Assembly product for order completed: {}", assemblyProducts);
        return assemblyProducts;
    }

    @PostMapping("/add")
    public void addQuantityOfProduct(@RequestBody AddProductToWarehouseRequest addingProductsQuantity) {
        log.info("==> POST /api/v1/warehouse/add. Add quantity of product to warehouse: {}", addingProductsQuantity);
        warehouseService.addingProductsQuantity(addingProductsQuantity);
        log.info("||| POST /api/v1/warehouse/add. Add quantity of product to warehouse: completed");
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        log.info("==> GET /api/v1/warehouse/address. Getting warehouse address");
        AddressDto addressDto = warehouseService.getWarehouseAddress();
        log.info("<== GET /api/v1/warehouse/address. Warehouse address: {}", addressDto);
        return addressDto;
    }
}