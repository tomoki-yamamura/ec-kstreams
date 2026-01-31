package com.example.cartservice.domain.cart.value;

import java.util.UUID;
import org.springframework.util.Assert;

public record CartId(String value) {
  public CartId {
    Assert.hasText(value, "CartId cannot be empty");
  }

  public static CartId generate() {
    return new CartId(UUID.randomUUID().toString());
  }
}
