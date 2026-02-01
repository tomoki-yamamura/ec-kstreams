package com.example.cartservice.application.usecase;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;
import com.example.cartservice.infrastructure.stream.CartTopology;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CartUseCase {

  private final KafkaTemplate<String, CartCommand> kafkaTemplate;

  public CartUseCase(KafkaTemplate<String, CartCommand> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void addItem(String userIdStr, String itemIdStr, int quantityVal) {
    UserId userId = new UserId(userIdStr);
    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.AddItem(userId, itemId, quantity);
    sendToKafka(userId.value(), command);
  }

  public void removeItem(String userIdStr, String itemIdStr, int quantityVal) {
    UserId userId = new UserId(userIdStr);
    ItemId itemId = new ItemId(itemIdStr);
    Quantity quantity = new Quantity(quantityVal);

    CartCommand command = new CartCommand.RemoveItem(userId, itemId, quantity);
    sendToKafka(userId.value(), command);
  }

  public void submitCart(String userIdStr) {
    UserId userId = new UserId(userIdStr);

    CartCommand command = new CartCommand.SubmitCart(userId);
    sendToKafka(userId.value(), command);
  }

  private void sendToKafka(String key, CartCommand command) {
    CompletableFuture<?> future = kafkaTemplate.send(CartTopology.COMMANDS_TOPIC, key, command);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        System.out.println("Command sent successfully: " + command);
      } else {
        System.err.println("Failed to send command: " + ex.getMessage());
      }
    });
  }
}
