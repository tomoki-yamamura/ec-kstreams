package com.example.cartservice.domain.cart.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartIdTest {

  @Test
  @DisplayName("Should create CartId successfully with a valid string")
  void createValidCartId() {
    // Arrange
    String validValue = "cart-123";

    // Act
    CartId cartId = new CartId(validValue);

    // Assert
    assertThat(cartId.value()).isEqualTo(validValue);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is null")
  void cannotCreateWithNull() {
    // Act & Assert
    // Assert.hasText checks for null
    assertThatThrownBy(() -> new CartId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("CartId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is empty")
  void cannotCreateWithEmptyString() {
    // Act & Assert
    // Assert.hasText checks for empty string ""
    assertThatThrownBy(() -> new CartId(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("CartId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is blank (whitespace only)")
  void cannotCreateWithWhitespace() {
    // Act & Assert
    // Assert.hasText checks for whitespace " "
    assertThatThrownBy(() -> new CartId("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("CartId cannot be empty");
  }
}
