package com.example.cartservice.application.controller;

import com.example.cartservice.application.controller.dto.request.AddItemRequest;
import com.example.cartservice.application.controller.dto.response.CartOperationResponse;
import com.example.cartservice.application.usecase.command.CartCommandUseCase;
import com.example.cartservice.application.usecase.query.CartQueryUseCase;
import com.example.cartservice.domain.cart.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

  private final CartCommandUseCase cartCommandUseCase;
  private final CartQueryUseCase cartQueryUseCase;

  public CartController(CartCommandUseCase cartCommandUseCase, CartQueryUseCase cartQueryUseCase) {
    this.cartCommandUseCase = cartCommandUseCase;
    this.cartQueryUseCase = cartQueryUseCase;
  }

  @GetMapping("/{cartId}")
  public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
    Cart cart = cartQueryUseCase.getCart(cartId);
    return ResponseEntity.ok(cart);
  }

  @PostMapping("/items")
  public ResponseEntity<CartOperationResponse> addItem(@RequestBody AddItemRequest request) {

    String resolvedCartId = cartCommandUseCase.addItem(
        request.cartId(),
        request.userId(),
        request.itemId(),
        request.quantity());

    return ResponseEntity.ok(new CartOperationResponse(resolvedCartId, "Accepted"));
  }

  @DeleteMapping("/{cartId}/items/{itemId}")
  public ResponseEntity<CartOperationResponse> removeItem(
      @PathVariable String cartId,
      @PathVariable String itemId,
      @RequestParam(required = false) String userId,
      @RequestParam(defaultValue = "1") int quantity) {

    cartCommandUseCase.removeItem(cartId, userId, itemId, quantity);

    return ResponseEntity.ok(new CartOperationResponse(cartId, "Remove Accepted"));
  }

  @PostMapping("/{cartId}/submit")
  public ResponseEntity<CartOperationResponse> submitCart(
      @PathVariable String cartId,
      @RequestParam(required = false) String userId) {

    cartCommandUseCase.submitCart(cartId, userId);

    return ResponseEntity.ok(new CartOperationResponse(cartId, "Submitted"));
  }
}