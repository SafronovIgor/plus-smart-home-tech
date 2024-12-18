package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.entity.ShoppingCartProduct;
import ru.yandex.practicum.entity.ShoppingCartProductId;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.repository.ShoppingCartProductRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    ShoppingCartService self;
    ShoppingCartMapper cartMapper;
    WarehouseClient warehouseClient;
    ShoppingCartRepository cartRepository;
    ShoppingCartProductRepository cartProductsRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto get(String username) {
        checkUsernameForEmpty(username);

        ShoppingCart shoppingCart = cartRepository.findByUsernameIgnoreCaseAndActivated(username, true)
                .orElseThrow(() -> new NotAuthorizedUserException("No cart for username: " + username));

        List<ShoppingCartProduct> cartProductList =
                cartProductsRepository.findAllByCartProductIdShoppingCartId(shoppingCart.getShoppingCartId());

        return cartMapper.toShoppingCartDto(shoppingCart, cartProductList);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> products) {
        checkUsernameForEmpty(username);

        UUID cartId = cartRepository.findByUsernameIgnoreCaseAndActivated(username, true)
                .map(ShoppingCart::getShoppingCartId)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .username(username)
                            .activated(true)
                            .build();
                    cartRepository.save(newCart);
                    return newCart.getShoppingCartId();
                });

        List<ShoppingCartProduct> newCartProducts = mapToCartProducts(cartId, products);
        cartProductsRepository.saveAll(newCartProducts);

        return self.get(username);
    }

    @Override
    public void deactivate(String username) {
        checkUsernameForEmpty(username);

        ShoppingCart shoppingCart = cartRepository.findByUsernameIgnoreCaseAndActivated(username, true)
                .orElseThrow(() -> new NotAuthorizedUserException("No cart for username: " + username));

        shoppingCart.setActivated(false);
    }

    @Override
    public ShoppingCartDto update(String username, Map<UUID, Long> products) {
        checkUsernameForEmpty(username);

        ShoppingCartDto currentShoppingCart = self.get(username);
        UUID cartId = currentShoppingCart.shoppingCartId();

        List<ShoppingCartProduct> currentCartProducts = mapToCartProducts(cartId, currentShoppingCart.products());
        cartProductsRepository.deleteAll(currentCartProducts);

        List<ShoppingCartProduct> newCartProducts = mapToCartProducts(cartId, products);
        List<ShoppingCartProduct> savedCartProducts = cartProductsRepository.saveAll(newCartProducts);

        return cartMapper.toShoppingCartDto(cartMapper.toShoppingCart(currentShoppingCart, username),
                savedCartProducts);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        checkUsernameForEmpty(username);
        log.info("Attempting to change product quantity for user: {}", username);

        ShoppingCartDto currentShoppingCart = self.get(username);
        UUID cartId = currentShoppingCart.shoppingCartId();

        ShoppingCartProductId productId = new ShoppingCartProductId(cartId, UUID.fromString(request.productId()));
        ShoppingCartProduct cartProduct = cartProductsRepository.findById(productId)
                .orElseThrow(() -> new NoProductsInShoppingCartException(
                        "Product with id " + request.productId() + " not found")
                );

        log.info("Current quantity of product {} for user {}: {}", productId, username, cartProduct.getQuantity());
        cartProduct.setQuantity(request.newQuantity());

        log.info("Updated cart for user {}: {}", username, self.get(username));
        return self.get(username);
    }

    @Override
    public BookedProductsDto book(String username) {
        try {
            return warehouseClient.checkForProductsSufficiency(self.get(username));
        } catch (Exception e) {
            log.error("Error booking cart for user: {}", username, e);
            throw new RuntimeException("Failed to book products for username: " + username, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getById(String cartId) {
        ShoppingCart shoppingCart = cartRepository.findById(UUID.fromString(cartId))
                .orElseThrow(() -> new NotFoundException("Cart with id " + cartId + " not found"));

        List<ShoppingCartProduct> cartProductList =
                cartProductsRepository.findAllByCartProductIdShoppingCartId(UUID.fromString(cartId));

        return cartMapper.toShoppingCartDto(shoppingCart, cartProductList);
    }

    private void checkUsernameForEmpty(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty");
        }
    }

    private List<ShoppingCartProduct> mapToCartProducts(UUID cartId, Map<UUID, Long> products) {
        return products.entrySet().stream()
                .map(entry -> new ShoppingCartProduct(
                        new ShoppingCartProductId(cartId, entry.getKey()),
                        entry.getValue())
                )
                .collect(Collectors.toList());
    }
}