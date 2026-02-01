package com.example.cartservice.infrastructure.stream;

import com.example.cartservice.domain.cart.Cart;
import com.example.cartservice.domain.cart.command.CartCommand;
import com.example.cartservice.domain.cart.event.CartEvent;
import com.example.cartservice.domain.cart.value.CartId;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafkaStreams
public class CartTopology {

  public static final String COMMANDS_TOPIC = "cart-commands";
  public static final String EVENTS_TOPIC = "cart-events";
  public static final String SNAPSHOTS_STORE = "cart-snapshots";

  @Autowired
  public void buildTopology(StreamsBuilder builder) {

    Serde<String> stringSerde = Serdes.String();

    JsonSerde<CartCommand> commandSerde = new JsonSerde<>(CartCommand.class);
    JsonSerde<CartEvent> eventSerde = new JsonSerde<>(CartEvent.class);
    JsonSerde<Cart> cartSerde = new JsonSerde<>(Cart.class);

    commandSerde.configure(java.util.Map.of("spring.json.trusted.packages", "*"), false);
    eventSerde.configure(java.util.Map.of("spring.json.trusted.packages", "*"), false);
    cartSerde.configure(java.util.Map.of("spring.json.trusted.packages", "*"), false);

    KTable<String, Cart> cartTable = builder.stream(EVENTS_TOPIC, Consumed.with(stringSerde, eventSerde))
        .groupByKey()
        .aggregate(
            () -> null,
            (key, event, aggregate) -> {
              if (aggregate == null) {
                return Cart.empty(new CartId(key), event.userId()).apply(event);
              }
              return aggregate.apply(event);
            },
            Materialized.<String, Cart, KeyValueStore<Bytes, byte[]>>as(SNAPSHOTS_STORE)
                .withKeySerde(stringSerde)
                .withValueSerde(cartSerde));

    KStream<String, CartCommand> commandStream = builder.stream(COMMANDS_TOPIC,
        Consumed.with(stringSerde, commandSerde));

    commandStream
        .leftJoin(
            cartTable,
            (command, cart) -> {
              Cart currentCart = (cart != null) ? cart : Cart.empty(command.cartId(), command.userId());
              try {
                return currentCart.process(command);
              } catch (Exception e) {
                return null;
              }
            },
            Joined.with(stringSerde, commandSerde, cartSerde))
        .filter((key, event) -> event != null)
        .to(EVENTS_TOPIC, Produced.with(stringSerde, eventSerde));
  }
}
