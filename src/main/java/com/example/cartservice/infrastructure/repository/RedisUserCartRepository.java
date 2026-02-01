package com.example.cartservice.infrastructure.repository;

import com.example.cartservice.domain.cart.repository.UserCartRepository;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.UserId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RedisUserCartRepository implements UserCartRepository {

  private final StringRedisTemplate redisTemplate;

  private static final String KEY_PREFIX = "user:cart:";

  private static final Duration TTL = Duration.ofDays(30);

  public RedisUserCartRepository(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Optional<CartId> findActiveCartId(UserId userId) {
    String key = KEY_PREFIX + userId.value();
    String cartIdStr = redisTemplate.opsForValue().get(key);

    if (cartIdStr == null) {
      return Optional.empty();
    }
    return Optional.of(new CartId(cartIdStr));
  }

  @Override
  public void bindUserToCart(UserId userId, CartId cartId) {
    String key = KEY_PREFIX + userId.value();
    redisTemplate.opsForValue().set(key, cartId.value(), TTL);
  }
}
