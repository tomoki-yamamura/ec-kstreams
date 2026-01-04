package com.example.cartservice.domain.value;

import org.springframework.util.Assert;

public record ItemId(String value) {
  public ItemId {
    Assert.hasText(value, "ItemId cannot be empty");
  }
}
