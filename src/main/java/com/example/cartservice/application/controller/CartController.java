package com.example.cartservice.application.controller;

import com.example.cartservice.application.controller.dto.request.AddItemRequest;
import com.example.cartservice.application.controller.dto.response.CartOperationResponse;
import com.example.cartservice.application.usecase.CartUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

  private final CartUseCase cartUseCase;

  public CartController(CartUseCase cartUseCase) {
    this.cartUseCase = cartUseCase;
  }

  @PostMapping("/items")
  public ResponseEntity<CartOperationResponse> addItem(@RequestBody AddItemRequest request) {

    String resolvedCartId = cartUseCase.addItem(
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
      @RequestParam(required = false) String userId, // 監査ログ用(任意)
      @RequestParam(defaultValue = "1") int quantity) {

    cartUseCase.removeItem(cartId, userId, itemId, quantity);

    return ResponseEntity.ok(new CartOperationResponse(cartId, "Remove Accepted"));
  }

  @PostMapping("/{cartId}/submit")
  public ResponseEntity<CartOperationResponse> submitCart(
      @PathVariable String cartId,
      @RequestParam(required = false) String userId) {

    cartUseCase.submitCart(cartId, userId);

    return ResponseEntity.ok(new CartOperationResponse(cartId, "Submitted"));
  }
}
