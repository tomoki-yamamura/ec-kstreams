package com.example.cartservice.domain.event;

import com.example.cartservice.domain.value.ItemId;
import com.example.cartservice.domain.value.Quantity;
import com.example.cartservice.domain.value.UserId;

public record CartEvent(
    UserId userId,
    CartEventType type,
    ItemId itemId,
    Quantity quantity,
    long timestamp) {
  public enum CartEventType {
    ITEM_ADDED,
    ITEM_REMOVED,
    SUBMITTED
  }
}
