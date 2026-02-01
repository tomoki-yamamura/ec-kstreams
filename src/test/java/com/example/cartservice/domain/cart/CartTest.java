package com.example.cartservice.domain.cart;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.event.CartEvent;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

  private static final CartId CART_ID = new CartId("cart-001");
  private static final UserId USER_ID = new UserId("user-001");
  private static final ItemId ITEM_ID = new ItemId("item-123");
  private static final Quantity QTY_1 = new Quantity(1);
  private static final Quantity QTY_2 = new Quantity(2);

  @Nested
  @DisplayName("Decision Methods (process)")
  class ProcessTest {

    @Test
    @DisplayName("Should return ItemAdded event when adding item to active cart")
    void processAddItem() {
      Cart cart = Cart.empty(CART_ID, USER_ID);
      CartCommand.AddItem command = new CartCommand.AddItem(CART_ID, USER_ID, ITEM_ID, QTY_1);

      CartEvent.ItemAdded event = cart.process(command);

      assertThat(event).isNotNull();
      assertThat(event.itemId()).isEqualTo(ITEM_ID);
      assertThat(event.quantity()).isEqualTo(QTY_1);
      assertThat(event.userId()).isEqualTo(USER_ID);
      assertThat(event.occurredAt()).isNotNull();
    }

    @Test
    @DisplayName("Should return ItemRemoved event when removing existing item")
    void processRemoveItem() {
      CartItem existingItem = new CartItem(ITEM_ID, QTY_2);
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(ITEM_ID, existingItem), false);
      CartCommand.RemoveItem command = new CartCommand.RemoveItem(CART_ID, USER_ID, ITEM_ID, QTY_1);

      CartEvent.ItemRemoved event = cart.process(command);

      assertThat(event).isNotNull();
      assertThat(event.itemId()).isEqualTo(ITEM_ID);
      assertThat(event.quantity()).isEqualTo(QTY_1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when removing item that does not exist")
    void failRemoveItemNotFound() {
      Cart cart = Cart.empty(CART_ID, USER_ID);
      CartCommand.RemoveItem command = new CartCommand.RemoveItem(CART_ID, USER_ID, ITEM_ID, QTY_1);

      assertThatThrownBy(() -> cart.process(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Item not found");
    }

    @Test
    @DisplayName("Should return CartSubmitted event when submitting non-empty cart")
    void processSubmitCart() {
      CartItem existingItem = new CartItem(ITEM_ID, QTY_1);
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(ITEM_ID, existingItem), false);
      CartCommand.SubmitCart command = new CartCommand.SubmitCart(CART_ID, USER_ID);

      CartEvent.CartSubmitted event = cart.process(command);

      assertThat(event).isNotNull();
      assertThat(event.userId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when submitting empty cart")
    void failSubmitEmptyCart() {
      Cart cart = Cart.empty(CART_ID, USER_ID);
      CartCommand.SubmitCart command = new CartCommand.SubmitCart(CART_ID, USER_ID);

      assertThatThrownBy(() -> cart.process(command))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Cannot submit empty cart");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when modifying checked-out cart")
    void failWhenAlreadyCheckedOut() {
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(), true);
      CartCommand.AddItem command = new CartCommand.AddItem(CART_ID, USER_ID, ITEM_ID, QTY_1);

      assertThatThrownBy(() -> cart.process(command))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Cart is already checked out");
    }
  }

  @Nested
  @DisplayName("Evolution Methods (apply)")
  class ApplyTest {

    @Test
    @DisplayName("Should add new item to empty cart")
    void applyItemAddedNew() {
      Cart cart = Cart.empty(CART_ID, USER_ID);
      CartEvent.ItemAdded event = new CartEvent.ItemAdded(USER_ID, ITEM_ID, QTY_1, Instant.now());

      Cart updated = cart.apply(event);

      assertThat(updated.items()).hasSize(1);
      assertThat(updated.items().get(ITEM_ID).quantity()).isEqualTo(QTY_1);
      assertThat(updated).isNotSameAs(cart);
    }

    @Test
    @DisplayName("Should increase quantity when adding existing item")
    void applyItemAddedExisting() {
      CartItem existingItem = new CartItem(ITEM_ID, QTY_1);
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(ITEM_ID, existingItem), false);
      CartEvent.ItemAdded event = new CartEvent.ItemAdded(USER_ID, ITEM_ID, QTY_1, Instant.now());

      Cart updated = cart.apply(event);

      assertThat(updated.items()).hasSize(1);
      assertThat(updated.items().get(ITEM_ID).quantity()).isEqualTo(QTY_2);
    }

    @Test
    @DisplayName("Should decrease quantity when removing item partially")
    void applyItemRemovedPartial() {
      CartItem existingItem = new CartItem(ITEM_ID, QTY_2);
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(ITEM_ID, existingItem), false);
      CartEvent.ItemRemoved event = new CartEvent.ItemRemoved(USER_ID, ITEM_ID, QTY_1, Instant.now());

      Cart updated = cart.apply(event);

      assertThat(updated.items()).hasSize(1);
      assertThat(updated.items().get(ITEM_ID).quantity()).isEqualTo(QTY_1);
    }

    @Test
    @DisplayName("Should remove item from map when quantity becomes zero")
    void applyItemRemovedFully() {
      CartItem existingItem = new CartItem(ITEM_ID, QTY_1);
      Cart cart = new Cart(CART_ID, USER_ID, Map.of(ITEM_ID, existingItem), false);
      CartEvent.ItemRemoved event = new CartEvent.ItemRemoved(USER_ID, ITEM_ID, QTY_1, Instant.now());

      Cart updated = cart.apply(event);

      assertThat(updated.items()).isEmpty();
      assertThat(updated.items()).doesNotContainKey(ITEM_ID);
    }

    @Test
    @DisplayName("Should mark cart as checked out on CartSubmitted")
    void applyCartSubmitted() {
      Cart cart = Cart.empty(CART_ID, USER_ID);
      CartEvent.CartSubmitted event = new CartEvent.CartSubmitted(USER_ID, Instant.now());

      Cart updated = cart.apply(event);

      assertThat(updated.isCheckedOut()).isTrue();
    }
  }
}
