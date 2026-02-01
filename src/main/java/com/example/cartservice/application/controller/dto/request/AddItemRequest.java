package com.example.cartservice.application.controller.dto.request;

public record AddItemRequest(
    String cartId,
    String userId,
    String itemId,
    int quantity) {
}
