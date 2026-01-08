package com.example.cartservice.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

  @Test
  @DisplayName("Should create UserId successfully with a valid string")
  void createValidUserId() {
    // Arrange
    String validValue = "user-123";

    // Act
    UserId UserId = new UserId(validValue);

    // Assert
    assertThat(UserId.value()).isEqualTo(validValue);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is null")
  void cannotCreateWithNull() {
    // Act & Assert
    // Assert.hasText checks for null
    assertThatThrownBy(() -> new UserId(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("UserId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is empty")
  void cannotCreateWithEmptyString() {
    // Act & Assert
    // Assert.hasText checks for empty string ""
    assertThatThrownBy(() -> new UserId(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("UserId cannot be empty");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when value is blank (whitespace only)")
  void cannotCreateWithWhitespace() {
    // Act & Assert
    // Assert.hasText checks for whitespace " "
    assertThatThrownBy(() -> new UserId("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("UserId cannot be empty");
  }
}
