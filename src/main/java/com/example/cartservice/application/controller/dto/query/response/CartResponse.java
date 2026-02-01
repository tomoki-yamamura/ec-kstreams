package com.example.cartservice.application.controller.dto.query.response;

import com.example.cartservice.domain.cart.Cart;
import java.util.List;
import java.util.stream.Collectors;

public record CartResponse(
    String id,
    String userId,
    List<CartItemResponse> items,
    boolean checkedOut
) {
  public static CartResponse from(Cart cart) {
    List<CartItemResponse> itemList = cart.items().values().stream()
        .map(item -> new CartItemResponse(
            item.itemId().value(),
            item.quantity().value()
        ))
        .collect(Collectors.toList());

    return new CartResponse(
        cart.id().value(),
        (cart.userId() != null) ? cart.userId().value() : null,
        itemList,
        cart.isCheckedOut()
    );
  }

  public record CartItemResponse(
      String itemId,
      int quantity
  ) {}
}
