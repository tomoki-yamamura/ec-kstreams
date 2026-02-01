package com.example.cartservice.domain.cart.event;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;

import java.time.Instant;

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