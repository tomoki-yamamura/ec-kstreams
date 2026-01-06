package com.example.cartservice.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {
  @Test
  @DisplayName("Should create Quantity successfully with a natural number")
  void createValidQuantity() {
    // Arrange
    int value = 1;

    // Act
    Quantity quantity = new Quantity(value);

    // Assert
    assertThat(quantity.value()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should create Quantity successfully with a zero number")
  void createZeroQuantity() {
    // Arrange
    int value = 0;

    // Act
    Quantity quantity = new Quantity(value);

    // Assert
    assertThat(quantity.value()).isEqualTo(0);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when a negative value is provided")
  void cannotCreateNegativeQuantity() {
    assertThatThrownBy(() -> new Quantity(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Quantity cannot be negative");
  }

  @Test
  @DisplayName("Addition: Should add two Quantities and return a new instance")
  void addQuantity() {
    // Arrange
    Quantity q1 = new Quantity(10);
    Quantity q2 = new Quantity(5);

    // Act
    Quantity result = q1.add(q2);

    // Assert
    assertThat(result.value()).isEqualTo(15);
  }
}
