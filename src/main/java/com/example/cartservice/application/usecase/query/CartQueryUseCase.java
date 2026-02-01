package com.example.cartservice.application.usecase.query;

import com.example.cartservice.domain.cart.Cart;
import com.example.cartservice.domain.cart.repository.CartQueryRepository;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.UserId;
import org.springframework.stereotype.Service;

@Service
public class CartQueryUseCase {

    private final CartQueryRepository queryRepository;

    public CartQueryUseCase(CartQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Cart getCart(String cartIdStr) {
        CartId cartId = new CartId(cartIdStr);

        return queryRepository.findById(cartId)
                .orElseGet(() -> {
                    return Cart.empty(cartId, new UserId("unknown"));
                });
    }
}
