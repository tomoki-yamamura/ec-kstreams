package com.example.cartservice.domain.cart;

import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

  private static final ItemId ITEM_ID = new ItemId("item-123");
  private static final Quantity QTY_1 = new Quantity(1);
  private static final Quantity QTY_2 = new Quantity(2);
  private static final Quantity QTY_3 = new Quantity(3);

  @Nested
  @DisplayName("Constructor")
  class ConstructorTest {

    @Test
    @DisplayName("Should create CartItem successfully with valid arguments")
    void createSuccessfully() {
      CartItem cartItem = new CartItem(ITEM_ID, QTY_1);

      assertThat(cartItem).isNotNull();
      assertThat(cartItem.itemId()).isEqualTo(ITEM_ID);
      assertThat(cartItem.quantity()).isEqualTo(QTY_1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ItemId is null")
    void failWhenItemIdIsNull() {
      assertThatThrownBy(() -> new CartItem(null, QTY_1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ItemId must not be null");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when Quantity is null")
    void failWhenQuantityIsNull() {
      assertThatThrownBy(() -> new CartItem(ITEM_ID, null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Quantity must not be null");
    }
  }

  @Nested
  @DisplayName("increase")
  class IncreaseTest {

    @Test
    @DisplayName("Should return new CartItem with increased quantity")
    void increaseSuccessfully() {
      CartItem initial = new CartItem(ITEM_ID, QTY_1);
      CartItem result = initial.increase(QTY_2);

      assertThat(result).isNotSameAs(initial);
      assertThat(result.quantity()).isEqualTo(QTY_3);
      assertThat(result.itemId()).isEqualTo(ITEM_ID);

      assertThat(initial.quantity()).isEqualTo(QTY_1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when added quantity is null")
    void failWhenArgIsNull() {
      CartItem initial = new CartItem(ITEM_ID, QTY_1);

      assertThatThrownBy(() -> initial.increase(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Quantity to add must not be null");
    }
  }

  @Nested
  @DisplayName("decrease")
  class DecreaseTest {

    @Test
    @DisplayName("Should return new CartItem with decreased quantity")
    void decreaseSuccessfully() {
      CartItem initial = new CartItem(ITEM_ID, QTY_3);
      CartItem result = initial.decrease(QTY_1);

      assertThat(result).isNotSameAs(initial);
      assertThat(result.quantity()).isEqualTo(QTY_2);
      assertThat(result.itemId()).isEqualTo(ITEM_ID);

      assertThat(initial.quantity()).isEqualTo(QTY_3);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when removed quantity is null")
    void failWhenArgIsNull() {
      CartItem initial = new CartItem(ITEM_ID, QTY_3);

      assertThatThrownBy(() -> initial.decrease(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Quantity to remove must not be null");
    }
  }
}
