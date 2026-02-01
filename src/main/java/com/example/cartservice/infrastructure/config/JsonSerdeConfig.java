package com.example.cartservice.infrastructure.config;

import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.event.CartEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSerdeConfig {

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "type"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = CartCommand.AddItem.class, name = "AddItem"),
      @JsonSubTypes.Type(value = CartCommand.RemoveItem.class, name = "RemoveItem"),
      @JsonSubTypes.Type(value = CartCommand.SubmitCart.class, name = "SubmitCart")
  })
  interface CartCommandMixin {
  }

  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.PROPERTY,
      property = "type"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = CartEvent.ItemAdded.class, name = "ItemAdded"),
      @JsonSubTypes.Type(value = CartEvent.ItemRemoved.class, name = "ItemRemoved"),
      @JsonSubTypes.Type(value = CartEvent.CartSubmitted.class, name = "CartSubmitted")
  })
  interface CartEventMixin {
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();

    mapper.addMixIn(CartCommand.class, CartCommandMixin.class);
    mapper.addMixIn(CartEvent.class, CartEventMixin.class);

    return mapper;
  }
}
