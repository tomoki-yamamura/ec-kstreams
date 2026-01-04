package com.example.cartservice.domain.value;

import org.springframework.util.Assert;

public record UserId(String value) {
  public UserId {
    Assert.hasText(value, "UserId cannot be empty");
  }
}
