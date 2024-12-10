package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.config.WarehouseAddress;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.entity.Dimension;
import ru.yandex.practicum.entity.ReservedProduct;
import ru.yandex.practicum.entity.WarehouseProduct;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.BookedProductsMapper;
import ru.yandex.practicum.mapper.DimensionMapper;
import ru.yandex.practicum.repository.ReservedProductsRepository;
import ru.yandex.practicum.repository.WarehouseProductRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private static final long PRODUCT_LIMIT = 150;
    private final WarehouseProductRepository warehouseProductRepository;
    private final ReservedProductsRepository reservedProductsRepository;
    private final DimensionMapper dimensionMapper;
    private final BookedProductsMapper bookedProductsMapper;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    @Transactional
    public void addNewProduct(NewProductInWarehouseRequest newProduct) {
        validateProductAbsence(newProduct.productId());

        Dimension dimension = dimensionMapper.fromDto(newProduct.dimension());

        WarehouseProduct warehouseProduct = WarehouseProduct.builder()
                .productId(UUID.fromString(newProduct.productId()))
                .fragile(newProduct.fragile())
                .dimension(dimension)
                .weight(newProduct.weight())
                .quantity(0L)
                .build();

        warehouseProductRepository.save(warehouseProduct);
    }

    @Override
    @Transactional
    public void returnProducts(Map<String, Long> products) {
        Map<UUID, WarehouseProduct> productMap = findWarehouseProducts(products);

        productMap.forEach((productId, product) ->
                product.setQuantity(product.getQuantity() + products.get(productId.toString())));

        warehouseProductRepository.saveAll(productMap.values());
        log.info("Returned products: {}", products.keySet());
    }

    @Override
    @Transactional
    public BookedProductsDto bookProducts(ShoppingCartDto shoppingCart) {
        Map<UUID, WarehouseProduct> currentProducts = findWarehouseProducts(shoppingCart.products());
        validateProductsAvailability(currentProducts, shoppingCart.products());
        List<ReservedProduct> reservedProductsList = shoppingCart.products().entrySet().stream()
                .map(entry -> reserveProduct(shoppingCart.shoppingCartId(), entry))
                .toList();
        reservedProductsRepository.saveAll(reservedProductsList);
        updateShoppingStoreStates(reservedProductsList);
        return bookedProductsMapper.toBookedProductsDto(reservedProductsList, currentProducts);
    }

    private void validateProductAbsence(String productId) {
        if (warehouseProductRepository.existsById(UUID.fromString(productId))) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already exists in warehouse");
        }
    }

    private Map<UUID, WarehouseProduct> findWarehouseProducts(Map<String, Long> products) {
        List<UUID> productIds = products.keySet()
                .stream()
                .map(UUID::fromString)
                .toList();
        List<WarehouseProduct> warehouseProducts = warehouseProductRepository.findAllByProductIdIn(productIds);
        return warehouseProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, product -> product));
    }

    private void validateProductsAvailability(Map<UUID, WarehouseProduct> currentProducts, Map<String, Long> products) {
        products.forEach((id, qty) -> {
            UUID uuid = UUID.fromString(id);
            WarehouseProduct product = currentProducts.get(uuid);
            if (product == null || product.getQuantity() < qty) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products in warehouse");
            }
        });
    }

    private ReservedProduct reserveProduct(String cartId, Map.Entry<String, Long> entry) {
        return ReservedProduct.builder()
                .shoppingCartId(UUID.fromString(cartId))
                .productId(UUID.fromString(entry.getKey()))
                .reservedQuantity(entry.getValue())
                .build();
    }

    private void updateShoppingStoreStates(List<ReservedProduct> reservedProductsList) {
        reservedProductsList.forEach(reservedProduct -> shoppingStoreClient.changeProductState(
                new SetProductQuantityStateRequest(
                        reservedProduct.getProductId().toString(),
                        specifyQuantityState(reservedProduct.getReservedQuantity()))));
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductForOrderFromShoppingCartRequest assembly) {
        List<ReservedProduct> reservedProducts = reservedProductsRepository.findAllByShoppingCartId(
                UUID.fromString(assembly.shoppingCartId()));
        Map<UUID, WarehouseProduct> warehouseProductMap = warehouseProductRepository.findAllByProductIdIn(
                        reservedProducts.stream().map(ReservedProduct::getReservedProductId).toList())
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, product -> product));
        reservedProducts.forEach(reservedProduct -> adjustProductQuantity(reservedProduct,
                warehouseProductMap));
        warehouseProductRepository.saveAll(warehouseProductMap.values());
        return bookedProductsMapper.toBookedProductsDto(reservedProducts, warehouseProductMap);
    }

    private void adjustProductQuantity(ReservedProduct reservedProduct,
                                       Map<UUID, WarehouseProduct> warehouseProductMap) {
        UUID reservedProductId = reservedProduct.getReservedProductId();
        WarehouseProduct product = warehouseProductMap.get(reservedProductId);
        product.setQuantity(product.getQuantity() - reservedProduct.getReservedQuantity());
    }

    @Override
    @Transactional
    public void addingProductsQuantity(AddProductToWarehouseRequest addingProductsQuantity) {
        WarehouseProduct warehouseProduct = findWarehouseProduct(addingProductsQuantity.productId());
        warehouseProduct.setQuantity(warehouseProduct.getQuantity() + addingProductsQuantity.quantity());
        warehouseProductRepository.save(warehouseProduct);

        shoppingStoreClient.changeProductState(
                new SetProductQuantityStateRequest(
                        warehouseProduct.getProductId().toString(),
                        specifyQuantityState(warehouseProduct.getQuantity())));

        log.info("Added product with id: {}, by quantity {}. New quantity: {}",
                addingProductsQuantity.productId(),
                addingProductsQuantity.quantity(),
                warehouseProduct.getQuantity());
    }

    private WarehouseProduct findWarehouseProduct(String productId) {
        return warehouseProductRepository.findByProductId(UUID.fromString(productId))
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id: " + productId + " not found in warehouse"));
    }

    @Override
    public AddressDto getWarehouseAddress() {
        WarehouseAddress warehouseAddress = new WarehouseAddress();
        return AddressDto.builder()
                .country(warehouseAddress.getCountry())
                .city(warehouseAddress.getCity())
                .street(warehouseAddress.getStreet())
                .house(warehouseAddress.getHouse())
                .flat(warehouseAddress.getFlat())
                .build();
    }

    private QuantityState specifyQuantityState(long quantity) {
        if (quantity == 0) {
            return QuantityState.ENDED;
        } else if (quantity >= PRODUCT_LIMIT * 0.75) {
            return QuantityState.MANY;
        } else if (quantity >= PRODUCT_LIMIT * 0.5) {
            return QuantityState.ENOUGH;
        } else {
            return QuantityState.FEW;
        }
    }
}