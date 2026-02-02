package com.example.cartservice.application.controller;

import com.example.cartservice.application.usecase.command.CartCommandUseCase;
import com.example.cartservice.application.usecase.query.CartQueryUseCase;
import com.example.cartservice.domain.cart.Cart;
import com.example.cartservice.domain.cart.CartItem;
import com.example.cartservice.domain.cart.value.CartId;
import com.example.cartservice.domain.cart.value.ItemId;
import com.example.cartservice.domain.cart.value.Quantity;
import com.example.cartservice.domain.cart.value.UserId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CartQueryUseCase cartQueryUseCase;

  @MockBean
  private CartCommandUseCase cartCommandUseCase;

  @Test
  @DisplayName("カート取得: 存在するIDを指定した場合、カート情報と200 OKを返す")
  void getCart_ReturnCartResponse() throws Exception {
    // Given
    String targetCartId = "cart-123";
    String targetUserId = "user-999";
    String targetItemId = "item-001";
    int targetQuantity = 2;

    CartId cartId = new CartId(targetCartId);
    UserId userId = new UserId(targetUserId);
    ItemId itemId = new ItemId(targetItemId);
    Quantity quantity = new Quantity(targetQuantity);

    CartItem cartItem = new CartItem(itemId, quantity);
    Map<ItemId, CartItem> items = new HashMap<>();
    items.put(itemId, cartItem);

    Cart cart = new Cart(cartId, userId, items, false);

    given(cartQueryUseCase.getCart(targetCartId)).willReturn(cart);

    // When & Then
    mockMvc.perform(get("/api/carts/{cartId}", targetCartId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(targetCartId))
        .andExpect(jsonPath("$.userId").value(targetUserId))
        .andExpect(jsonPath("$.checkedOut").value(false))
        .andExpect(jsonPath("$.items[0].itemId").value(targetItemId))
        .andExpect(jsonPath("$.items[0].quantity").value(targetQuantity));
  }

  @Test
  @DisplayName("アイテム追加: 正常なリクエストで、カートIDと受付ステータスを返す")
  void addItem_ReturnAcceptedResponse() throws Exception {
    // Given
    String expectedCartId = "cart-new-123";
    given(cartCommandUseCase.addItem(any(), anyString(), anyString(), anyInt()))
        .willReturn(expectedCartId);

    String requestJson = """
        {
          "cartId": null,
          "userId": "user-001",
          "itemId": "item-apple",
          "quantity": 3
        }
        """;

    // When & Then
    mockMvc.perform(post("/api/carts/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cartId").value(expectedCartId))
        .andExpect(jsonPath("$.status").value("Accepted"));
  }

  @Test
  @DisplayName("カート取得: 空のカートでも正しくJSONを返す")
  void getCart_EmptyCart_ReturnCartResponse() throws Exception {
    // Given
    String targetCartId = "cart-empty";
    String targetUserId = "user-empty";

    CartId cartId = new CartId(targetCartId);
    UserId userId = new UserId(targetUserId);
    Cart emptyCart = new Cart(cartId, userId, new HashMap<>(), false);

    given(cartQueryUseCase.getCart(targetCartId)).willReturn(emptyCart);

    // When & Then
    mockMvc.perform(get("/api/carts/{cartId}", targetCartId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(targetCartId))
        .andExpect(jsonPath("$.userId").value(targetUserId))
        .andExpect(jsonPath("$.checkedOut").value(false))
        .andExpect(jsonPath("$.items").isArray())
        .andExpect(jsonPath("$.items").isEmpty());
  }
}