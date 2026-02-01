package com.example.cartservice.domain.cart.repository;

import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.UserId;

import java.util.Optional;

public interface UserCartRepository {

  Optional<CartId> findActiveCartId(UserId userId);

  void bindUserToCart(UserId userId, CartId cartId);
}
