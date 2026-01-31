package com.example.cartservice.domain.cart.command;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;

public sealed interface CartCommand {
  UserId userId();

  record AddItem(UserId userId, ItemId itemId, Quantity quantity) implements CartCommand {
  }

  record RemoveItem(UserId userId, ItemId itemId, Quantity quantity) implements CartCommand {
  }

  record SubmitCart(UserId userId) implements CartCommand {
  }
}
