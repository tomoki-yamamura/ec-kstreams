package com.example.cartservice.application.usecase;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.repository.CartCommandRepository;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import org.springframework.stereotype.Service;

@Service
public class CartUseCase {

  private final CartCommandRepository commandRepository;

  public CartUseCase(CartCommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  public void addItem(String userIdStr, String itemIdStr, int quantityVal) {
    UserId userId = new UserId(userIdStr);
    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.AddItem(userId, itemId, quantity);
    commandRepository.save(command);
  }

  public void removeItem(String userIdStr, String itemIdStr, int quantityVal) {
    UserId userId = new UserId(userIdStr);
    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.RemoveItem(userId, itemId, quantity);
    commandRepository.save(command);
  }

  public void submitCart(String userIdStr) {
    UserId userId = new UserId(userIdStr);

    CartCommand command = new CartCommand.SubmitCart(userId);
    commandRepository.save(command);
  }
}
