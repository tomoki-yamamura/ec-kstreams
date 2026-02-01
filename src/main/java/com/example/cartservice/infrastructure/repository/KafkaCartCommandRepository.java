package com.example.cartservice.infrastructure.repository;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.repository.CartCommandRepository;
import com.example.cartservice.infrastructure.stream.CartTopology;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public class KafkaCartCommandRepository implements CartCommandRepository {

  private final KafkaTemplate<String, CartCommand> kafkaTemplate;

  public KafkaCartCommandRepository(KafkaTemplate<String, CartCommand> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void save(CartCommand command) {
    String key = command.userId().value();

    CompletableFuture<?> future = kafkaTemplate.send(CartTopology.COMMANDS_TOPIC, key, command);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        System.out.println("Command saved successfully: " + command);
      } else {
        System.err.println("Failed to save command: " + ex.getMessage());
      }
    });
  }
}
