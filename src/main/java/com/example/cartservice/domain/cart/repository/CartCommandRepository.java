package com.example.cartservice.domain.cart.repository;

import com.example.cartservice.domain.cart.command.CartCommand;

public interface CartCommandRepository {
  void save(CartCommand command);
}
