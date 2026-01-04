package com.example.cartservice.domain.aggregate;

import com.example.cartservice.domain.command.CartCommand;
import com.example.cartservice.domain.event.CartEvent;
import com.example.cartservice.domain.value.ItemId;
import com.example.cartservice.domain.value.Quantity;
import com.example.cartservice.domain.value.UserId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class Cart {

  private UserId userId;
  private final Map<ItemId, Quantity> items;
  private boolean submitted;

  public Cart(UserId userId) {
    this.userId = userId;
    this.items = new HashMap<>();
    this.submitted = false;
  }

  protected Cart() {
    this.items = new HashMap<>();
  }

  public CartEvent execute(CartCommand command) {
    return switch (command) {
      case CartCommand.AddItem cmd -> {
        yield new CartEvent(cmd.userId(), CartEvent.CartEventType.ITEM_ADDED, cmd.itemId(), cmd.quantity(),
            System.currentTimeMillis());
      }
      case CartCommand.RemoveItem cmd -> {
        yield new CartEvent(cmd.userId(), CartEvent.CartEventType.ITEM_REMOVED, cmd.itemId(), cmd.quantity(),
            System.currentTimeMillis());
      }
      case CartCommand.SubmitCart cmd -> {
        if (this.items.isEmpty()) {
          throw new IllegalStateException("Cannot submit an empty cart");
        }
        yield new CartEvent(cmd.userId(), CartEvent.CartEventType.SUBMITTED, null, new Quantity(0),
            System.currentTimeMillis());
      }
    };
  }

  public Cart apply(CartEvent event) {
    if (this.userId == null) {
      this.userId = event.userId();
    }

    if (this.submitted) {
      this.items.clear();
      this.submitted = false;
    }

    switch (event.type()) {
      case ITEM_ADDED -> {
        items.compute(event.itemId(),
            (id, current) -> (current == null) ? event.quantity() : current.add(event.quantity()));
      }
      case ITEM_REMOVED -> {
        items.computeIfPresent(event.itemId(), (id, current) -> {
          int remaining = current.value() - event.quantity().value();
          return (remaining <= 0) ? null : new Quantity(remaining);
        });
      }
      case SUBMITTED -> {
        this.submitted = true;
      }
    }
    return this;
  }

  public Map<ItemId, Quantity> getItems() {
    return Collections.unmodifiableMap(items);
  }
}
