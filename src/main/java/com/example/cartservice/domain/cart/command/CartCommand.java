package com.example.cartservice.domain.cart.command;

import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;

public sealed interface CartCommand {
  
  CartId cartId();
  UserId userId();

  record AddItem(
      CartId cartId,
      UserId userId, 
      ItemId itemId, 
      Quantity quantity
  ) implements CartCommand {}

  record RemoveItem(
      CartId cartId,
      UserId userId, 
      ItemId itemId, 
      Quantity quantity
  ) implements CartCommand {}

  record SubmitCart(
      CartId cartId,
      UserId userId
  ) implements CartCommand {}
}
