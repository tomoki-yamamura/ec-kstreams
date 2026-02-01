package com.example.cartservice.domain.cart.repository;

import com.example.cartservice.domain.cart.Cart;
import com.example.cartservice.domain.cart.value.CartId;

import java.util.Optional;

public interface CartQueryRepository {
  Optional<Cart> findById(CartId cartId);
}
