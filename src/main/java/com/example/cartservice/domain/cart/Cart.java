package com.example.cartservice.domain.cart;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.event.CartEvent;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.UserId;

public record Cart(
    CartId id,
    UserId userId,
    Map<ItemId, CartItem> items,
    boolean isCheckedOut) {

  public static Cart empty(CartId id, UserId userId) {
    return new Cart(id, userId, new HashMap<>(), false);
  }

  public CartEvent process(CartCommand command) {
    return switch (command) {
      case CartCommand.AddItem c -> process(c);
      case CartCommand.RemoveItem c -> process(c);
      case CartCommand.SubmitCart c -> process(c);
    };
  }

  public CartEvent.ItemAdded process(CartCommand.AddItem command) {
    verifyCartIsActive();

    return new CartEvent.ItemAdded(
        this.userId,
        command.itemId(),
        command.quantity(),
        Instant.now());
  }

  public CartEvent.ItemRemoved process(CartCommand.RemoveItem command) {
    verifyCartIsActive();

    if (!this.items.containsKey(command.itemId())) {
      throw new IllegalArgumentException("Item not found in cart: " + command.itemId());
    }

    return new CartEvent.ItemRemoved(
        this.userId,
        command.itemId(),
        command.quantity(),
        Instant.now());
  }

  public CartEvent.CartSubmitted process(CartCommand.SubmitCart command) {
    verifyCartIsActive();

    if (this.items.isEmpty()) {
      throw new IllegalStateException("Cannot submit empty cart");
    }
    return new CartEvent.CartSubmitted(this.userId, Instant.now());
  }

  private void verifyCartIsActive() {
    if (this.isCheckedOut) {
      throw new IllegalStateException("Cart is already checked out");
    }
  }

  public Cart apply(CartEvent event) {
    return switch (event) {
      case CartEvent.ItemAdded e -> apply(e);
      case CartEvent.ItemRemoved e -> apply(e);
      case CartEvent.CartSubmitted e -> apply(e);
    };
  }

  private Cart apply(CartEvent.ItemAdded event) {
    Map<ItemId, CartItem> newItems = new HashMap<>(this.items);

    newItems.compute(event.itemId(), (id, existingItems) -> {
      if (existingItems == null) {
        return new CartItem(id, event.quantity());
      }
      return existingItems.increase(event.quantity());
    });

    return new Cart(this.id, this.userId, newItems, false);
  };

  private Cart apply(CartEvent.ItemRemoved event) {
    Map<ItemId, CartItem> newItems = new HashMap<>(this.items);

    newItems.computeIfPresent(event.itemId(), (id, existingItems) -> {
      CartItem decreased = existingItems.decrease(event.quantity());

      if (decreased.quantity().isZero() || decreased.quantity().value() < 0) {
        return null;
      }
      return decreased;
    });

    return new Cart(this.id, this.userId, newItems, false);
  }

  private Cart apply(CartEvent.CartSubmitted event) {
    return new Cart(this.id, this.userId, this.items, true);
  }
}
