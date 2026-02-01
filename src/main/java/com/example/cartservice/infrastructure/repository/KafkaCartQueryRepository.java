package com.example.cartservice.infrastructure.repository;

import com.example.cartservice.domain.cart.Cart;
import com.example.cartservice.domain.cart.repository.CartQueryRepository;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.infrastructure.stream.CartTopology;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class KafkaCartQueryRepository implements CartQueryRepository {

  private final StreamsBuilderFactoryBean streamsFactory;

  public KafkaCartQueryRepository(StreamsBuilderFactoryBean streamsFactory) {
    this.streamsFactory = streamsFactory;
  }

  @Override
  public Optional<Cart> findById(CartId cartId) {
    KafkaStreams streams = streamsFactory.getKafkaStreams();
    if (streams == null) {
      return Optional.empty();
    }

    try {
      ReadOnlyKeyValueStore<String, Cart> store = streams.store(
          StoreQueryParameters.fromNameAndType(
              CartTopology.SNAPSHOTS_STORE,
              QueryableStoreTypes.keyValueStore()));

      Cart cart = store.get(cartId.value());
      return Optional.ofNullable(cart);

    } catch (InvalidStateStoreException e) {
      return Optional.empty();
    }
  }
}
