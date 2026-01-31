package com.example.cartservice.domain.cart;

import org.springframework.util.Assert;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;

public record CartItem(ItemId itemId, Quantity quantity) {
  public CartItem {
    Assert.notNull(itemId, "ItemId must not be null");
    Assert.notNull(quantity, "Quantity must not be null");
  }

  public CartItem increase(Quantity addQty) {
    Assert.notNull(addQty, "Quantity to add must not be null");
    return new CartItem(this.itemId, this.quantity.add(addQty));
  }

  public CartItem decrease(Quantity removeQty) {
    Assert.notNull(removeQty, "Quantity to remove must not be null");
    return new CartItem(this.itemId, this.quantity.subtract(removeQty));
  }
}
