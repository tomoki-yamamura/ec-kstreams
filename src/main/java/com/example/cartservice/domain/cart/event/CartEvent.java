package com.example.cartservice.domain.cart.event;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CartEvent.ItemAdded.class, name = "ItemAdded"),
    @JsonSubTypes.Type(value = CartEvent.ItemRemoved.class, name = "ItemRemoved"),
    @JsonSubTypes.Type(value = CartEvent.CartSubmitted.class, name = "CartSubmitted")
})
public sealed interface CartEvent {

  UserId userId();

  Instant occurredAt();

  record ItemAdded(
      UserId userId,
      ItemId itemId,
      Quantity quantity,
      Instant occurredAt) implements CartEvent {
  }

  record ItemRemoved(
      UserId userId,
      ItemId itemId,
      Quantity quantity,
      Instant occurredAt) implements CartEvent {
  }

  record CartSubmitted(
      UserId userId,
      Instant occurredAt) implements CartEvent {
  }
}