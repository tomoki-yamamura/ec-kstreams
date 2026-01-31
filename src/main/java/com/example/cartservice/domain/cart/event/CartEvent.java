package com.example.cartservice.domain.cart.event;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;

public sealed interface CartEvent {
  UserId userId();

  record ItemAdded(UserId userId, ItemId itemId, Quantity quantity) implements CartEvent {
  }

  record ItemRemoved(UserId userId, ItemId itemId, Quantity quantity) implements CartEvent {
  }

  record CartSubmitted(UserId userId) implements CartEvent {
  }
}
