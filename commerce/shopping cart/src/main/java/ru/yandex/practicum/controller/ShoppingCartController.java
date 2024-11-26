package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @GetMapping
    public ShoppingCartDto get(@RequestParam String username) {
        ShoppingCartDto shoppingCart = cartService.get(username);
        log.info("Retrieved shopping cart for user: {}", username);
        return shoppingCart;
    }

    @PutMapping
    public ShoppingCartDto addNewProductsToCart(@RequestParam String username,
                                                @RequestBody Map<String, Long> products) {
        ShoppingCartDto shoppingCart = cartService.addProducts(username, products);
        log.info("Added products to shopping cart for user: {}", username);
        return shoppingCart;
    }

    @DeleteMapping
    public void deactivate(@RequestParam String username) {
        cartService.deactivate(username);
        log.info("Deactivated cart for user: {}", username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto updateCart(@RequestParam String username,
                                      @RequestBody Map<String, Long> products) {
        ShoppingCartDto shoppingCart = cartService.update(username, products);
        log.info("Updated cart for user: {}", username);
        return shoppingCart;
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantityOfProduct(@RequestParam String username,
                                                   @RequestBody ChangeProductQuantityRequest request) {
        ShoppingCartDto shoppingCart = cartService.changeProductQuantity(username, request);
        log.info("Changed product quantity for user: {}", username);
        return shoppingCart;
    }

    @PostMapping("/booking")
    public BookedProductsDto book(@RequestParam String username) {
        BookedProductsDto bookedProducts = cartService.book(username);
        log.info("Booked cart for user: {}", username);
        return bookedProducts;
    }

    @GetMapping("/{cartId}")
    public ShoppingCartDto getShoppingCartById(@PathVariable String cartId) {
        ShoppingCartDto shoppingCart = cartService.getById(cartId);
        log.info("Retrieved cart by ID: {}", cartId);
        return shoppingCart;
    }
}