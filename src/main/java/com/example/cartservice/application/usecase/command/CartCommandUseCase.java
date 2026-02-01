package com.example.cartservice.application.usecase.command;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.repository.CartCommandRepository;
import com.example.cartservice.domain.cart.repository.UserCartRepository;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartCommandUseCase {

  private final CartCommandRepository commandRepository;
  private final UserCartRepository userCartRepository;

  public CartCommandUseCase(CartCommandRepository commandRepository,
      UserCartRepository userCartRepository) {
    this.commandRepository = commandRepository;
    this.userCartRepository = userCartRepository;
  }

  public String addItem(String cartIdStr, String userIdStr, String itemIdStr, int quantityVal) {
    UserId userId = new UserId(userIdStr);
    CartId cartId;

    if (cartIdStr != null && !cartIdStr.isBlank()) {
      cartId = new CartId(cartIdStr);
    } else {
      cartId = userCartRepository.findActiveCartId(userId)
          .orElseGet(() -> {
            CartId newId = new CartId(UUID.randomUUID().toString());
            if (userIdStr != null && !userIdStr.isBlank()) {
              userCartRepository.bindUserToCart(userId, newId);
            }
            return newId;
          });
    }

    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.AddItem(cartId, userId, itemId, quantity);

    commandRepository.save(command);

    return cartId.value();
  }

  public void removeItem(String cartIdStr, String userIdStr, String itemIdStr, int quantityVal) {
    if (cartIdStr == null || cartIdStr.isBlank()) {
      throw new IllegalArgumentException("CartId is required for removing items.");
    }

    CartId cartId = new CartId(cartIdStr);
    UserId userId = new UserId(userIdStr);
    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.RemoveItem(cartId, userId, itemId, quantity);

    commandRepository.save(command);
  }

  public void submitCart(String cartIdStr, String userIdStr) {
    if (cartIdStr == null || cartIdStr.isBlank()) {
      throw new IllegalArgumentException("CartId is required for submitting cart.");
    }

    CartId cartId = new CartId(cartIdStr);
    UserId userId = new UserId(userIdStr);

    CartCommand command = new CartCommand.SubmitCart(cartId, userId);

    commandRepository.save(command);
  }
}
