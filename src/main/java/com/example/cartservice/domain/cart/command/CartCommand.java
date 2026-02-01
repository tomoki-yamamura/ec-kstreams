package com.example.cartservice.domain.cart.command;

import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CartCommand.AddItem.class, name = "AddItem"),
    @JsonSubTypes.Type(value = CartCommand.RemoveItem.class, name = "RemoveItem"),
    @JsonSubTypes.Type(value = CartCommand.SubmitCart.class, name = "SubmitCart")
})
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
