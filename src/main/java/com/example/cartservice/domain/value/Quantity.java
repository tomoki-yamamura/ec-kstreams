package com.example.cartservice.domain.value;

import org.springframework.util.Assert;

public record Quantity(int value) {
  public Quantity {
    Assert.isTrue(value >= 0, "Quantity cannot be negative");
  }

  public Quantity add(Quantity other) {
    return new Quantity(this.value + other.value());
  }
}
