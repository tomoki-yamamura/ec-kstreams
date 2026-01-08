package com.example.cartservice.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemIdTest {

  @Test
  @DisplayName("Should create ItemId successfully with a valid string")
  void createValidItemId() {
    // Arrange
    String validValue = "item-123";

    // Act
    ItemId itemId = new ItemId(validValue);

    // Assert
    assertThat(itemId.value()).isEqualTo(validValue);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is null")
  void cannotCreateWithNull() {
    // Act & Assert
    // Assert.hasText checks for null
    assertThatThrownBy(() -> new ItemId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ItemId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is empty")
  void cannotCreateWithEmptyString() {
    // Act & Assert
    // Assert.hasText checks for empty string ""
    assertThatThrownBy(() -> new ItemId(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ItemId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is blank (whitespace only)")
  void cannotCreateWithWhitespace() {
    // Act & Assert
    // Assert.hasText checks for whitespace " "
    assertThatThrownBy(() -> new ItemId("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ItemId cannot be empty");
  }
}