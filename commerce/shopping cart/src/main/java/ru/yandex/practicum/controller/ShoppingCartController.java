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
        log.info("Retrieved shopping cart for user: {}", username);
        return cartService.get(username);
    }

    @PutMapping
    public ShoppingCartDto addNewProductsToCart(@RequestParam String username,
                                                @RequestBody Map<String, Long> products) {
        log.info("Added products to shopping cart for user: {}", username);
        return cartService.addProducts(username, products);
    }

    @DeleteMapping
    public void deactivate(@RequestParam String username) {
        cartService.deactivate(username);
        log.info("Deactivated cart for user: {}", username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto updateCart(@RequestParam String username, @RequestBody Map<String, Long> products) {
        log.info("Updated cart for user: {}", username);
        return cartService.update(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantityOfProduct(@RequestParam String username,
                                                   @RequestBody ChangeProductQuantityRequest request) {
        log.info("Changed product quantity for user: {}", username);
        return cartService.changeProductQuantity(username, request);
    }

    @PostMapping("/booking")
    public BookedProductsDto book(@RequestParam String username) {
        log.info("Booked cart for user: {}", username);
        return cartService.book(username);
    }

    @GetMapping("/{cartId}")
    public ShoppingCartDto getShoppingCartById(@PathVariable String cartId) {
        log.info("Retrieved cart by ID: {}", cartId);
        return cartService.getById(cartId);
    }
}